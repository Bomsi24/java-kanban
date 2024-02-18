package com.yandex.test.service.manager;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final String fileSaveHistory = "save.csv";
    private final String fileSaveClear = "saveClear.csv";

    @BeforeEach
    public void createInMemoryTasksManagerTest() {
        this.manager = new FileBackedTasksManager(fileSaveHistory);
    }

    @Test
    public void loadFromFileClearTask() {
        final FileBackedTasksManager newBackedTasksManagerClear = FileBackedTasksManager.loadFromFile(fileSaveClear);
        final List<Task> tasksClear = newBackedTasksManagerClear.getAllTasks();
        Assertions.assertEquals(0, tasksClear.size(), "Список задач не пуст");
    }

    @Test
    public void loadFromFileEpicWithoutSubtasks() {
        Epic epic = manager.create(new Epic("Эпик1", "Описание", 0, null));
        final FileBackedTasksManager newBackedTasksManagerClear = FileBackedTasksManager.loadFromFile(fileSaveHistory);
        final List<Epic> epics = newBackedTasksManagerClear.getAllEpics();
        Assertions.assertEquals(1, epics.size(), "Список задач не пуст");
    }

    @Test
    public void loadFromFileTest() {
        final Task task1 = manager.create(new Task("Купить молоко", "Сходить в магазин", 10, "07:00 06.01.24"));

        manager.getTask(task1.getId());

        FileBackedTasksManager newBackedTasksManager = FileBackedTasksManager.loadFromFile(fileSaveHistory);

        final Task task2 = newBackedTasksManager.getTask(task1.getId());

        Assertions.assertEquals(task1.toString(), task2.toString(), "Задачи не равны");

    }
}
