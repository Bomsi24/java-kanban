package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    public int create(Task task);

    public int create(Epic epic);

    public int create(SubTask subTask);

    public ArrayList<Task> getAllTasks();

    public ArrayList<Epic> getAllEpics();

    public ArrayList<SubTask> getAllSubTasks();

    public void deleteTasks();

    public void deleteEpics();

    public void deleteSubTasks();

    public Task getTask(Integer id);

    public Epic getEpic(Integer id);

    public SubTask getSubTask(Integer id);

    public void deleteTaskInId(Integer id);

    public void deleteEpicInId(Integer id);

    public void deleteSubTaskInId(Integer id);

    public void update(Task task);

    public void update(SubTask subTask);

    public void update(Epic epic);

    public void updateStatusEpic(Epic epic);

    public List<Task> getHistory();

}
