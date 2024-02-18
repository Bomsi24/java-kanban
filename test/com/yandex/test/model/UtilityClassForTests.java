package com.yandex.test.model;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

public class UtilityClassForTests {

    public static Epic epic1() {
        return new Epic("Эпик1", "Описание", 0, null);
    }

    public static Epic epic2() {
        return new Epic("Эпик2", "Описание", 0, null);
    }

    public static SubTask subTask1(Epic epic) {
        return new SubTask("Первая подзадача", "Описание", epic, 0, null);
    }

    public static SubTask subTask2(Epic epic) {
        return new SubTask("Вторая подзадача", "Описание", epic, 0, null);
    }

    public static Task task1() {
        return new Task("Таск1", "Описание", 0, null);
    }

    public static Task task2() {
        return new Task("Таск2", "Описание", 0, null);
    }
}
