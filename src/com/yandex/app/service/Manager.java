package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int idTask = 0;//идентификатор

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void createTask(Task task) {
        task.setId(addId());
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(addId());
        epics.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        subTask.setId(addId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpicById(subTask.getEpicId());
        epic.setSubTasks(subTask.getId());
        updateStatusEpic(epic);//обновление статуса эпика
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());

    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();// удаляем подзадачи потому что их без эпика несуществует
    }

    public void deleteSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTask();
            updateStatusEpic(epic);
        }
    }

    public Task getTaskById(Integer id) {
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                if (i.equals(id)) {
                    return tasks.get(i);
                }
            }
        }
        return null;
    }

    public Epic getEpicById(Integer id) {
        if (!epics.isEmpty()) {
            for (Integer i : epics.keySet()) {
                if (i.equals(id)) {
                    return epics.get(i);
                }
            }
        }
        return null;
    }

    public SubTask getSubTaskById(Integer id) {
        if (!subTasks.isEmpty()) {
            for (Integer i : subTasks.keySet()) {
                if (i.equals(id)) {
                    return subTasks.get(i);
                }
            }
        }
        return null;
    }

    public void deleteTaskInId(Integer id) {
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                if (i.equals(id)) {
                    tasks.remove(i);
                }
            }
        }

    }

    public void deleteEpicInId(Integer id) {
        if (!epics.isEmpty()) {
            for (Integer i : epics.keySet()) {
                if (i.equals(id)) {
                    for (Integer index : epics.get(i).getSubTasks()) {
                        subTasks.remove(index);
                    }
                    epics.remove(i);
                }
            }

        }

    }

    public void deleteSubTaskInId(Integer id) {
        if (!subTasks.isEmpty()) {
            for (Integer i : subTasks.keySet()) {
                if (i.equals(id)) {
                    Epic epic = getEpicById(subTasks.get(i).getEpicId());
                    subTasks.remove(i);
                    epic.deleteElementSubTask(i);
                    updateEpic(epic);

                }
            }
        }
    }


    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }


    public void updateStatusEpic(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(epic.getStatus(0));

        } else {
            int statusNew = 0;
            int statusInProgress = 0;
            int statusDone = 0;
            for (Integer subTaskId : epic.getSubTasks()) {
                switch (getSubTaskById(subTaskId).getStatus()) {
                    case "NEW":
                        statusNew++;
                        break;
                    case "IN_PROGRESS":
                        statusInProgress++;
                        break;
                    case "DONE":
                        statusDone++;
                        break;
                }
            }
            if (statusDone == 0 && statusInProgress == 0) {
                epic.setStatus(epic.getStatus(0));
            } else if (statusNew == 0 && statusInProgress == 0) {
                epic.setStatus(epic.getStatus(2));
            } else {
                epic.setStatus(epic.getStatus(1));
            }
        }
    }

    public void updateSubTask(SubTask subTask, String status) {
        subTask.setStatus(status);
        subTasks.put(subTask.getId(), subTask);
        updateStatusEpic(getEpicById(subTask.getEpicId()));

    }


    public int addId() {
        idTask++;
        return idTask;
    }
}

