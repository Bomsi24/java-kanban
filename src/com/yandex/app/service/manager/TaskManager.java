package com.yandex.app.service.manager;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

import java.util.List;

public interface TaskManager {
    Task create(Task task);

    Epic create(Epic epic);

    SubTask create(SubTask subTask);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    SubTask getSubTask(Integer id);

    void deleteTaskInId(Integer id);

    void deleteEpicInId(Integer id);

    void deleteSubTaskInId(Integer id);

    void update(Task task);

    void update(SubTask subTask);

    void update(Epic epic);

    void updateStatusEpic(Epic epic);

    List<Task> getHistory();

}
