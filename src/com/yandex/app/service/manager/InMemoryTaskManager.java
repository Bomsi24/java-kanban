package com.yandex.app.service.manager;

import com.yandex.app.model.*;
import com.yandex.app.service.history.HistoryManager;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idTask = 0;//идентификатор
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(new StartTimeComparator());

    @Override
    public Task create(Task task) {
        if (task.getId() < 0) {
            task.setId(addId());
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);

        return task;

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
        if (subTask.getId() < 0) {
            subTask.setId(addId());
        }
        subTasks.put(subTask.getId(), subTask);
        prioritizedTasks.add(subTask);
        Epic epic = subTask.getEpicOfSubTask();
        epic.setSubTasks(subTask);
        updateStatusEpic(epic);

        return subTask;


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
            prioritizedTasks.remove(tasks.get(idTask));
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer idEpic : epics.keySet()) {
            historyManager.remove(idEpic);
            for (SubTask idSubTask : epics.get(idEpic).getSubTasks()) {
                historyManager.remove(idSubTask.getId());
                prioritizedTasks.remove(subTasks.get(idSubTask));
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
            tasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }

    }

    @Override
    public void deleteEpicInId(Integer id) {
        if (!epics.isEmpty()) {
            for (SubTask index : epics.get(id).getSubTasks()) {
                subTasks.remove(index.getId());
                historyManager.remove(index.getId());
                prioritizedTasks.remove(subTasks.get(index));
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
            subTasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
            updateStatusEpic(epic);
        }
    }

    @Override
    public void update(Task task) {
        if (taskValidation(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }


    }

    @Override
    public void update(SubTask subTask) {
        if (taskValidation(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = subTask.getEpicOfSubTask();
            epic.setSubTasks(subTask);
            prioritizedTasks.add(subTask);
            updateTimesEpic(epic);
        }

    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateStatusEpic(Epic epic) {
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
        String startTimeString = null;
        LocalDateTime endTime = null;
        String endTimeString = null;
        long duration = 0;

        for (SubTask subTask : subTasksList) {
            if (subTask.getDuration() < 0) {
                continue;
            }

            if (startTime == null || subTask.getStartTime().isBefore(startTime)) { //проверка на начало
                startTime = subTask.getStartTime();
                startTimeString = subTask.getStartTimeToString();
            }
            if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
                endTimeString = subTask.getEndTimeToString();
            }

            duration = duration + subTask.getDuration();
        }
        if (startTime != null) {
            epic.createTime(duration, startTimeString);
            epic.setEndTime(endTime);
        }


    }

    public List<Task> getPrioritizedTasks() {

        return new ArrayList<>(prioritizedTasks);
    }

    public boolean taskValidation(Task task) {

        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        if (getPrioritizedTasks().contains(task)) {
            return false;
        }
        if (task.getDuration() < 0) {
            return true;
        }

        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        LocalDateTime preTime = null; //финальное время, предыдущей задачи

        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getDuration() < 0) {
                continue;
            }
            LocalDateTime startTimeTaskValid = prioritizedTask.getStartTime();
            LocalDateTime endTimeTaskValid = prioritizedTask.getEndTime();

            if ((startTime.isBefore(startTimeTaskValid) && (endTime.isBefore(startTimeTaskValid) ||
                    endTime.equals(endTimeTaskValid))) &&
                    ((preTime == null) || preTime.isAfter(startTime))) {
                return true;

            } else {
                preTime = endTimeTaskValid;
            }
        }
        return false;

    }

    private int addId() {
        idTask++;
        return idTask;
    }
}

