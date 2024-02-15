package com.yandex.test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Statuses;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.LocalDateTime;

class EpicTest {

    TaskManager manager;

     Epic epic;
     SubTask subTask1;
     SubTask subTask2;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("Эпик задача", "Описание",0,null);
        manager = Managers.getDefault();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/saveClear.csv")
    public void testOfEpicStatus( String status1, String status2, String statusEpic) {
        subTask1 = new SubTask("Первая подзадача", "Описание", epic,0,null);
        subTask2 = new SubTask("Вторая подзадача", "Описание", epic,0,null);

        subTask1.setStatus(Statuses.valueOf(status1));
        subTask2.setStatus(Statuses.valueOf(status2));
        manager.update(subTask1);
        manager.update(subTask2);

        Statuses epicStatus = epic.getCurrentStatus();
        Statuses statuses = Statuses.valueOf(statusEpic);

        Assertions.assertEquals(epicStatus,statuses,"Статусы не совпадают");

    }

    @Test
    public void emptyListOfSubtasks() {

        int subTasksNull = epic.getSubTasks().size();//проверка, что в эпике нет Саб Тасков.
        Assertions.assertEquals(0,subTasksNull,"В эпике есть Саб таски");

        Statuses epicStatus = epic.getCurrentStatus();
        Statuses statuses = Statuses.NEW;

        Assertions.assertEquals(epicStatus,statuses);

    }

    @Test
    public void epicTimeWithoutSubtasks() {
        epic = new Epic("Эпи1","Описание",0,null);
        LocalDateTime timeEpic = epic.getEndTime();

        Assertions.assertNull(timeEpic,"Неправильное время");

    }

    @Test
    public void epicTimeWithSubtasks() {
        epic = new Epic("Эпи1","Описание",0,null);
        subTask1 = new SubTask("Саб Таск1","Описание",epic,0,null);
        subTask2 = new SubTask("Саб таск2","Описание",epic,0,null);
        subTask1.createTime(20, "08:00 06.01.24");
        subTask2.createTime(30, "07:55 06.01.24");
        manager.update(subTask1);
        manager.update(subTask2);

        LocalDateTime timeEpicEndTime = epic.getEndTime();
        LocalDateTime timeEpicStartTime = epic.getStartTime();
        LocalDateTime endTime = LocalDateTime.of(2024,1,6,8, 25);
        LocalDateTime startTime = LocalDateTime.of(2024,1,6,7, 55);

        Assertions.assertEquals(endTime,timeEpicEndTime,"Время не равно");
        Assertions.assertEquals(startTime,timeEpicStartTime,"Время не равно");
    }

}