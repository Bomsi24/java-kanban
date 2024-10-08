package com.yandex.app.service.manager;

import com.yandex.app.model.*;
import com.yandex.app.service.history.HistoryManager;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idTask = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
        if (o1.getStartTime() != null) {
            return -1;
        }
        if (o2.getStartTime() != null) {
            return 1;
        }
        return Integer.compare(o1.getId(), o2.getId());
    }));

    @Override
    public Task create(Task task) {
        if (taskValidation(task)) {
            if (task.getId() < 0) {
                task.setId(addId());
            }
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
            return task;
        }
        System.out.println("Задача " + task.getName() + " не создана");
        return null;
    }

    @Override
    public Epic create(Epic epic) {
        if (epic.getId() < 0) {
            epic.setId(addId());
        }
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask create(SubTask subTask) {

        if (taskValidation(subTask)) {
            if (subTask.getId() < 0) {
                subTask.setId(addId());
            }
            subTasks.put(subTask.getId(), subTask);
            Epic epic = subTask.getEpicOfSubTask();
            epic.setSubTasks(subTask);
            updateStatusEpic(epic);
            updateTimesEpic(epic);
            prioritizedTasks.add(subTask);
            return subTask;
        }
        System.out.println("Подзадача " + subTask.getName() + " не создана");

        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Integer idTask : tasks.keySet()) {
            historyManager.remove(idTask);
            Task task = tasks.get(idTask);
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer idEpic : epics.keySet()) {
            historyManager.remove(idEpic);
            for (SubTask idSubTask : epics.get(idEpic).getSubTasks()) {
                historyManager.remove(idSubTask.getId());
                prioritizedTasks.remove(subTasks.get(idSubTask.getId()));
            }
        }
        epics.clear();
        subTasks.clear();// удаляем подзадачи потому что их без эпика не существует
    }

    @Override
    public void deleteSubTasks() {
        for (Integer idSubTask : subTasks.keySet()) {
            historyManager.remove(idSubTask);
            prioritizedTasks.remove(subTasks.get(idSubTask));
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTask();
            updateStatusEpic(epic);
        }
    }

    @Override
    public Task getTask(Integer id) {
        if (!tasks.isEmpty() && tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        if (!epics.isEmpty() && epics.get(id) != null) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTask(Integer id) {
        if (!subTasks.isEmpty() && subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public void deleteTaskInId(Integer id) {
        if (!tasks.isEmpty()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicInId(Integer id) {
        if (!epics.isEmpty()) {
            for (SubTask index : epics.get(id).getSubTasks()) {
                prioritizedTasks.remove(subTasks.get(index.getId()));
                historyManager.remove(index.getId());
                subTasks.remove(index.getId());
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubTaskInId(Integer id) {
        if (!subTasks.isEmpty()) {
            Epic epic = subTasks.get(id).getEpicOfSubTask();
            epic.deleteElementSubTask(subTasks.get(id));
            historyManager.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
            subTasks.remove(id);
            updateStatusEpic(epic);
        }
    }

    @Override
    public Task update(Task task) {
        if (taskValidation(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
            return task;
        }
        return null;
    }

    @Override
    public SubTask update(SubTask subTask) {
        if (taskValidation(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = subTask.getEpicOfSubTask();
            epic.setSubTasks(subTask);
            updateStatusEpic(epic);
            prioritizedTasks.add(subTask);
            updateTimesEpic(epic);
            return subTask;
        }
        System.out.println("Подзадача " + subTask.getName() + " не обновлена");
        return null;
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void updateStatusEpic(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Statuses.NEW);
        } else {
            int statusNew = 0;
            int statusInProgress = 0;
            int statusDone = 0;

            for (SubTask subTaskId : epic.getSubTasks()) {
                switch (subTaskId.getCurrentStatus()) {
                    case NEW:
                        statusNew++;
                        break;
                    case IN_PROGRESS:
                        statusInProgress++;
                        break;
                    case DONE:
                        statusDone++;
                        break;
                }
            }
            if (statusDone == 0 && statusInProgress == 0) {
                epic.setStatus(Statuses.NEW);
            } else if (statusNew == 0 && statusInProgress == 0) {
                epic.setStatus(Statuses.DONE);
            } else {
                epic.setStatus(Statuses.IN_PROGRESS);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateTimesEpic(Epic epic) {
        List<SubTask> subTasksList = epic.getSubTasks();
        if (subTasksList.isEmpty()) {
            return;
        }

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        long duration = 0;

        for (SubTask subTask : subTasksList) {
            if (subTask.getDuration() == 0) {
                continue;
            }
            if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
            if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
            duration = duration + subTask.getDuration();
        }
        if (startTime != null) {
            epic.createTime(duration, startTime);
            epic.setEndTime(endTime);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean taskValidation(Task task) {
        return getPrioritizedTasks().stream()
                .filter(t -> t.getStartTime() != null)
                .filter(t -> t.getStartTime() != task.getStartTime())
                .noneMatch(t -> (t.getStartTime().isBefore(task.getEndTime()) &&
                        t.getEndTime().isAfter(task.getStartTime())));
    }

    private int addId() {
        idTask++;
        return idTask;
    }
}

