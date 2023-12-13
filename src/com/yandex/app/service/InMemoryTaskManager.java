package com.yandex.app.service;

import com.sun.source.tree.NewArrayTree;
import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idTask = 0;//идентификатор
    private HistoryManager historyManager = Managers.getDefaultHistory();

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    @Override
    public int create(Task task) {
        task.setId(addId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int create(Epic epic) {
        epic.setId(addId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int create(SubTask subTask) {
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        epic.setSubTasks(subTask.getId());
        updateStatusEpic(epic);//обновление статуса эпика
        return subTask.getId();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());

    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
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
        if (!tasks.isEmpty()) {
            for (Integer idTask : tasks.keySet()) {
                if (idTask.equals(id)) {
                    historyManager.add(tasks.get(idTask));
                    return tasks.get(idTask);
                }
            }
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        if (!epics.isEmpty()) {
            for (Integer idEpic : epics.keySet()) {
                if (idEpic.equals(id)) {
                    historyManager.add(epics.get(idEpic));
                    return epics.get(idEpic);
                }
            }
        }
        return null;
    }

    @Override
    public SubTask getSubTask(Integer id) {
        if (!subTasks.isEmpty()) {
            for (Integer idSubTask : subTasks.keySet()) {
                if (idSubTask.equals(id)) {
                    historyManager.add(subTasks.get(idSubTask));
                    return subTasks.get(idSubTask);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteTaskInId(Integer id) {
        if (!tasks.isEmpty()) {
            for (Integer idTask : tasks.keySet()) {
                if (idTask.equals(id)) {
                    tasks.remove(idTask);
                    return;
                }
            }
        }

    }

    @Override
    public void deleteEpicInId(Integer id) {
        if (!epics.isEmpty()) {
            for (Integer idEpic : epics.keySet()) {
                if (idEpic.equals(id)) {
                    for (Integer index : epics.get(idEpic).getSubTasks()) {
                        subTasks.remove(index);
                    }
                    epics.remove(idEpic);
                    return;
                }
            }

        }

    }

    @Override
    public void deleteSubTaskInId(Integer id) {
        if (!subTasks.isEmpty()) {
            for (Integer idSubTask : subTasks.keySet()) {
                if (idSubTask.equals(id)) {
                    Epic epic = getEpic(subTasks.get(idSubTask).getEpicId());
                    epic.deleteElementSubTask(idSubTask);
                    subTasks.remove(id);
                    updateStatusEpic(epic);
                    return;

                }
            }
        }
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
        historyManager.getHistory().clear();
    }

    @Override
    public void update(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        epic.setSubTasks(subTask.getId());
        updateStatusEpic(getEpic(subTask.getEpicId()));
        historyManager.getHistory().clear();
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        historyManager.getHistory().clear();
    }

    @Override
    public void updateStatusEpic(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(Task.Statuses.NEW);
        } else {
            int statusNew = 0;
            int statusInProgress = 0;
            int statusDone = 0;
            for (Integer subTaskId : epic.getSubTasks()) {
                if (getSubTask(subTaskId).getCurrentStatus() == Task.Statuses.NEW) {
                    statusNew++;
                } else if (getSubTask(subTaskId).getCurrentStatus() == Task.Statuses.IN_PROGRESS) {
                    statusInProgress++;
                } else if (getSubTask(subTaskId).getCurrentStatus() == Task.Statuses.DONE) {
                    statusDone++;
                }
            }
            if (statusDone == 0 && statusInProgress == 0) {
                epic.setStatus(Task.Statuses.NEW);
            } else if (statusNew == 0 && statusInProgress == 0) {
                epic.setStatus(Task.Statuses.DONE);
            } else {
                epic.setStatus(Task.Statuses.IN_PROGRESS);
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

