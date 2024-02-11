package com.yandex.test;

import com.yandex.app.model.Task;
import com.yandex.app.service.manager.InMemoryTaskManager;
import com.yandex.app.service.manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

   protected T manager;


    @Test
    public void createTest() {

        final Task task1 = new Task("Купить молоко", "Сходить в магазин");
        final Task taskTest = manager.create(task1);

        Assertions.assertNotNull(taskTest, "Задача не найдена");
        Assertions.assertEquals(taskTest, task1, "Задачи не совпадают");

        final List<Task> listTask = manager.getAllTasks();

        Assertions.assertNotNull(listTask, "Задача не возвращаются");
        Assertions.assertEquals(1, listTask.size(), "Неверное количество задач");
        Assertions.assertEquals(taskTest, listTask.get(0), "Задача не совпадают");

    }
}