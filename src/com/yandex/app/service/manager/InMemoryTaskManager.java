package com.yandex.app.service.manager;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Statuses;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idTask = 0;//идентификатор
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    @Override
    public Task create(Task task) {
        task.setId(addId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic create(Epic epic) {
        epic.setId(addId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask create(SubTask subTask) {
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpicOfSubTask();
        epic.setSubTasks(subTask);
        updateStatusEpic(epic);//обновление статуса эпика
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
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();// удаляем подзадачи потому что их без эпика несуществует
    }

    @Override
    public void deleteSubTasks() {
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
        }

    }

    @Override
    public void deleteEpicInId(Integer id) {
        if (!epics.isEmpty()) {
            for (SubTask index : epics.get(id).getSubTasks()) {
                subTasks.remove(index.getId());
                historyManager.remove(index.getId());
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
            historyManager.remove(subTasks.get(id).getId());//удаляем эпик
            subTasks.remove(id);
            historyManager.remove(id);
            updateStatusEpic(epic);
        }
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpicOfSubTask();
        epic.setSubTasks(subTask);
        updateStatusEpic(subTask.getEpicOfSubTask());
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

    public int addId() {
        idTask++;
        return idTask;
    }
}

