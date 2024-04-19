package com.yandex.test.model;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Statuses;
import com.yandex.app.model.SubTask;
import com.yandex.app.service.manager.InMemoryTaskManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.LocalDateTime;

class EpicTest {

    private TaskManager manager;

    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    public void beforeEach() {
        epic = UtilityClassForTests.epic1();
        manager = new InMemoryTaskManager();
        subTask1 = UtilityClassForTests.subTask1(epic);
        subTask2 = UtilityClassForTests.subTask2(epic);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/saveClear.csv")
    public void testOfEpicStatus(String status1, String status2, String statusEpic) {

        subTask1.setStatus(Statuses.valueOf(status1));
        subTask2.setStatus(Statuses.valueOf(status2));
        manager.update(subTask1);
        manager.update(subTask2);

        Statuses epicStatus = epic.getCurrentStatus();
        Statuses statuses = Statuses.valueOf(statusEpic);

        Assertions.assertEquals(epicStatus, statuses, "Статусы не совпадают");

    }

    @Test
    public void emptyListOfSubtasks() {
        int subTasksNull = epic.getSubTasks().size();//проверка, что в эпике нет Саб Тасков.
        Assertions.assertEquals(0, subTasksNull, "В эпике есть Саб таски");

        Statuses epicStatus = epic.getCurrentStatus();
        Statuses statuses = Statuses.NEW;

        Assertions.assertEquals(epicStatus, statuses);

    }

    @Test
    public void epicTimeWithoutSubtasks() {
        LocalDateTime timeEpic = epic.getEndTime();

        Assertions.assertNull(timeEpic, "Неправильное время");
    }

    @Test
    public void epicTimeBasedOnSubtasks() {
        subTask1.createTime(20, LocalDateTime.of(2024, 1, 6, 10, 00));
        manager.update(subTask1);
        subTask2.createTime(30, LocalDateTime.of(2024, 1, 6, 11, 30));
        manager.update(subTask2);

        LocalDateTime timeTestStart = LocalDateTime.of(2024, 1, 6, 10, 00);
        LocalDateTime timeTestEnd = LocalDateTime.of(2024, 1, 6, 12, 00);
        long durationTest = 50;
        LocalDateTime timeStartOfEpic = epic.getStartTime();
        LocalDateTime timeEndOfEpic = epic.getEndTime();
        long durationEpic = epic.getDuration();

        Assertions.assertEquals(timeTestStart, timeStartOfEpic, "Время начала не сходится");
        Assertions.assertEquals(timeTestEnd, timeEndOfEpic, "Время конца не сходится");
        Assertions.assertEquals(durationTest, durationEpic, "Продолжительность не сходится");
    }
}