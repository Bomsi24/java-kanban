package com.yandex.test.service.manager;

import com.yandex.app.model.Epic;
import com.yandex.app.model.StartTimeComparator;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import com.yandex.test.model.UtilityClassForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    protected Task task;
    protected Task task2;
    protected Epic epic;
    protected Epic epic2;
    protected SubTask subTask1;
    protected SubTask subTask2;

    @Test
    public void createTask() {

        final Task task = UtilityClassForTests.task1();
        final Task taskTest = manager.create(task);

        assertNotNull(taskTest, "Задача не найдена");
        assertEquals(taskTest, task, "Задачи не совпадают");

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задача не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(taskTest, tasks.get(0), "Задача не совпадают");

    }

    @Test
    public void testTimeOfTask() {
        task = UtilityClassForTests.task1();
        final LocalDateTime timeStartIsEmpty = task.getStartTime();
        final LocalDateTime timeEndIsEmpty = task.getEndTime();
        final long durationIsEmpty = task.getDuration();

        assertNull(timeStartIsEmpty,"Время старта не пусто");
        assertNull(timeEndIsEmpty,"Время конца не пусто");
        assertEquals(0, durationIsEmpty,"Продолжительность не нулевая");

        task.createTime(20, "10:00 06.01.24");

        final LocalDateTime timeStartNotEmpty = LocalDateTime.of(2024, 1, 6, 10, 00);
        final LocalDateTime timeEndNotEmpty = LocalDateTime.of(2024, 1, 6, 10, 20);
        final long durationNotEmpty = 20;

        final LocalDateTime timeStartTask = task.getStartTime();
        final LocalDateTime timeEndTask = task.getEndTime();
        final long durationTask = task.getDuration();

        assertEquals(timeStartTask,timeStartNotEmpty,"Время старта не совпадает");
        assertEquals(timeEndTask,timeEndNotEmpty,"Время конца не совпадает");
        assertEquals(durationTask,durationNotEmpty,"Продолжительность не совпадает");
    }

    @Test
    public void createEpic() {
        epic = UtilityClassForTests.epic1();
        epic2 = manager.create(epic);

        assertNotNull(epic2, "Эпик не найден");
        assertEquals(epic2, epic, "Эпик не найден");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпик не возвращается");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic2, epics.get(0), "Задачи не совпадают");
    }

    @Test
    public void createSub() {
        epic = UtilityClassForTests.epic1();
        assertNotNull(epic, "Пустой эпик");

        subTask1 = UtilityClassForTests.subTask1(epic);
        subTask2 = manager.create(subTask1);

        assertNotNull(subTask2, "СабТаск не найден");
        assertEquals(subTask2, subTask1, "СабТаск не найден");

        List<SubTask> subTasks = manager.getAllSubTasks();

        assertNotNull(subTasks, "СабТаск не возвращается");
        assertEquals(1, subTasks.size(), "Неверное количество СабТасков");
        assertEquals(subTask2, subTasks.get(0), "СабТаски не совпадают");
    }

    @Test
    public void testTimeOfSubTask() {
        epic = UtilityClassForTests.epic1();
        subTask1 = UtilityClassForTests.subTask1(epic);
        final LocalDateTime timeStartIsEmpty = subTask1.getStartTime();
        final LocalDateTime timeEndIsEmpty = subTask1.getEndTime();
        final long durationIsEmpty = subTask1.getDuration();

        assertNull(timeStartIsEmpty,"Время старта не пусто");
        assertNull(timeEndIsEmpty,"Время конца не пусто");
        assertEquals(0, durationIsEmpty,"Продолжительность не нулевая");

        subTask1.createTime(20, "10:00 06.01.24");

        final LocalDateTime timeStartNotEmpty = LocalDateTime.of(2024, 1, 6, 10, 00);
        final LocalDateTime timeEndNotEmpty = LocalDateTime.of(2024, 1, 6, 10, 20);
        final long durationNotEmpty = 20;

        final LocalDateTime timeStartTask = subTask1.getStartTime();
        final LocalDateTime timeEndTask = subTask1.getEndTime();
        final long durationTask = subTask1.getDuration();

        assertEquals(timeStartTask,timeStartNotEmpty,"Время старта не совпадает");
        assertEquals(timeEndTask,timeEndNotEmpty,"Время конца не совпадает");
        assertEquals(durationTask,durationNotEmpty,"Продолжительность не совпадает");
    }

    @Test
    public void getAllTasks() {
        task = manager.create(UtilityClassForTests.task1());
        task2 = manager.create(UtilityClassForTests.task2());


        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Список задач пуст");
        assertEquals(2, tasks.size(), "Неверное количество задач");
    }

    @Test
    public void getAllEpics() {
        epic = manager.create(UtilityClassForTests.epic1());
        epic2 = manager.create(UtilityClassForTests.epic2());

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Список задач пуст");
        assertEquals(2, epics.size(), "Неверное количество задач");
    }

    @Test
    public void getAllSubTasks() {
        epic = manager.create(UtilityClassForTests.epic1());
        assertNotNull(epic, "Пустой эпик");

        subTask1 = manager.create(UtilityClassForTests.subTask1(epic));
        subTask2 = manager.create(UtilityClassForTests.subTask2(epic));


        final List<SubTask> subTasks = manager.getAllSubTasks();

        assertNotNull(subTasks, "Список задач пуст");
        assertEquals(2, subTasks.size(), "Неверное количество задач");
    }

    @Test
    public void deleteTasks() {

        task = manager.create(UtilityClassForTests.task1());
        task2 = manager.create(UtilityClassForTests.task2());

        final List<Task> sortedTasks = manager.getPrioritizedTasks();
        final List<Task> tasks = manager.getAllTasks();

        manager.getTask(task.getId());
        manager.getTask(task2.getId());

        final List<Task> history = manager.getHistory();

        assertNotNull(tasks, "Задача не возвращаются");
        assertEquals(2, history.size(), "Неверный размер истории просмотров");
        assertEquals(2, tasks.size(), "Неверное количество задач,после добавления");
        assertEquals(2, sortedTasks.size(), "Неправильное количество задач в отсортированном списке");
        manager.deleteTasks();

        final List<Task> tasksClear = manager.getAllTasks();
        final List<Task> sortedTasksClear = manager.getPrioritizedTasks();
        final List<Task> historyClear = manager.getHistory();

        assertEquals(0, historyClear.size(), "История не пустая");
        assertEquals(0, tasksClear.size(), "Неверное количество задач,после удаления");
        assertEquals(0, sortedTasksClear.size(), "Отсортированный список задач не пуст");

    }

    @Test
    public void deleteEpics() {
        epic = manager.create(UtilityClassForTests.epic1());
        subTask1 = manager.create(UtilityClassForTests.subTask1(epic));
        subTask2 = manager.create(UtilityClassForTests.subTask2(epic));

        final List<Epic> epics = manager.getAllEpics();
        final List<SubTask> subTasks = manager.getAllSubTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        manager.getEpic(epic.getId());
        manager.getSubTask(subTask1.getId());
        manager.getSubTask(subTask2.getId());

        final List<Task> history = manager.getHistory();

        assertNotNull(epics, "Эпики не возвращаются");
        assertNotNull(subTasks, "Саб таски не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество Эпиков,после добавления");
        assertEquals(2, subTasks.size(), "Неверное количество Саб тасков после добавления");
        assertEquals(2, sortedTasks.size(), "Неправильное количество задач в отсортированном списке");
        assertEquals(3, history.size(), "Неверный размер истории просмотров");

        manager.deleteEpics();

        final List<Epic> epicsClear = manager.getAllEpics();
        final List<SubTask> subTasksClear = manager.getAllSubTasks();
        final List<Task> sortedTasksClear = manager.getPrioritizedTasks();
        final List<Task> historyClear = manager.getHistory();

        assertEquals(0, epicsClear.size(), "Эпики не удалены");
        assertEquals(0, subTasksClear.size(), "Саб таски не удалены");
        assertEquals(0, sortedTasksClear.size(), "Отсортированный список не пуст");
        assertEquals(0, historyClear.size(), "История просмотров не пуста");

    }

    @Test
    public void deleteSubTasks() {
        epic = manager.create(UtilityClassForTests.epic1());
        subTask1 = manager.create(UtilityClassForTests.subTask1(epic));
        subTask2 = manager.create(UtilityClassForTests.subTask2(epic));

        final List<SubTask> subTasks = manager.getAllSubTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();
        int sizeSubTaskOfEpic = epic.getSubTasks().size();

        manager.getSubTask(subTask1.getId());
        manager.getSubTask(subTask2.getId());

        final List<Task> history = manager.getHistory();

        assertNotNull(subTasks, "Саб таски не возвращаются");
        assertEquals(2, sizeSubTaskOfEpic, "Неверное количество Саб тасков в эпике");
        assertEquals(2, subTasks.size(), "Неверное количество Саб тасков после добавления");
        assertEquals(2, sortedTasks.size(), "Неправильное количество задач в отсортированном списке");
        assertEquals(2, history.size(), "Неверный размер истории просмотров");

        manager.deleteSubTasks();

        sizeSubTaskOfEpic = epic.getSubTasks().size();
        final List<SubTask> subTasksClear = manager.getAllSubTasks();
        final List<Task> sortedTasksClear = manager.getPrioritizedTasks();
        final List<Task> historyClear = manager.getHistory();

        assertEquals(0, sizeSubTaskOfEpic, "Неверное количество Саб тасков в эпике");
        assertEquals(0, subTasksClear.size(), "Саб таски не удалены");
        assertEquals(0, sortedTasksClear.size(), "Отсортированный список не пуст");
        assertEquals(0, historyClear.size(), "История просмотров не пуста");

    }

    @Test
    public void getTask() {
        task = manager.create(UtilityClassForTests.task2());
        task2 = UtilityClassForTests.task2();
        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "История не пуста");

        Task task1Test = manager.getTask(task.getId());
        Task task2Test = manager.getTask(task2.getId());

        final List<Task> historyClear = manager.getHistory();

        assertEquals(task, task1Test, "Задачи не равны");
        assertEquals(1, historyClear.size(), "Неверный размер истории");
        assertNotNull(task1Test, "Неверно возвращается задача созданная через менеджер");
        assertNull(task2Test, "Неверна возвращается задача созданный без менеджера");

    }

    @Test
    public void getEpic() {
        epic = manager.create(UtilityClassForTests.epic1());
        epic2 = UtilityClassForTests.epic2();
        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "История не пуста");

        Epic epic1Test = manager.getEpic(epic.getId());
        Epic epic2Test = manager.getEpic(epic2.getId());

        final List<Task> historyClear = manager.getHistory();

        assertEquals(epic, epic1Test, "Задачи не равны");
        assertEquals(1, historyClear.size(), "Неверный размер истории");
        assertNotNull(epic1Test, "Неверно возвращается Эпик созданная через менеджер");
        assertNull(epic2Test, "Неверна возвращается Эпик созданный без менеджера");

    }

    @Test
    public void getSubTask() {
        epic = manager.create(UtilityClassForTests.epic1());
        subTask1 = manager.create(UtilityClassForTests.subTask1(epic));
        subTask2 = UtilityClassForTests.subTask2(epic);
        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "История не пуста");

        SubTask subTask1Test = manager.getSubTask(subTask1.getId());
        SubTask subTask2Test = manager.getSubTask(subTask2.getId());

        final List<Task> historyClear = manager.getHistory();

        assertEquals(subTask1, subTask1Test, "Задачи не равны");
        assertEquals(1, historyClear.size(), "Неверный размер истории");
        assertNotNull(subTask1Test, "Неверно возвращается Эпик созданная через менеджер");
        assertNull(subTask2Test, "Неверна возвращается Эпик созданный без менеджера");
    }

    @Test
    public void deleteTaskInId() {
        task = manager.create(UtilityClassForTests.task1());
        task2 = manager.create(UtilityClassForTests.task2());
        final List<Task> tasks = manager.getAllTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(2, tasks.size(), "Неправильное количество в списке задач");
        assertEquals(2, sortedTasks.size(), "Неверное количество в списке отсортированных задач");

        manager.getTask(task.getId());
        manager.getTask(task2.getId());

        final List<Task> history = manager.getHistory();

        assertEquals(2, history.size(), "Неверное количество задач в истории");

        manager.deleteTaskInId(task.getId());

        final List<Task> tasksAfterDeletion = manager.getAllTasks();
        final List<Task> historyAfterDeletion = manager.getHistory();
        final List<Task> sortedTasksAfterDeletion = manager.getPrioritizedTasks();

        final boolean deleteTask = tasksAfterDeletion.contains(task);

        assertFalse(deleteTask, "Задача не удалена из списка задач");
        assertEquals(1, tasksAfterDeletion.size(), "Неверное количество задач в списке задач");
        assertEquals(1, historyAfterDeletion.size(), "Неверное количество задач в истории просмотров");
        assertEquals(1, sortedTasksAfterDeletion.size(),
                "Неверное количество задач в отсортированном списке");
    }

    @Test
    public void deleteEpicInId() {
        epic = manager.create(UtilityClassForTests.epic1());
        subTask1 = manager.create(UtilityClassForTests.subTask1(epic));
        subTask2 = manager.create(UtilityClassForTests.subTask2(epic));
        final List<Epic> epics = manager.getAllEpics();
        final List<SubTask> subTasks = manager.getAllSubTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(1, epics.size(), "Неправильное количество в списке Эпиков'");
        assertEquals(2, subTasks.size(), "Неправильное количество в списке Саб тасков'");
        assertEquals(2, sortedTasks.size(), "Неверное количество в списке отсортированных задач");

        manager.getEpic(epic.getId());
        manager.getSubTask(subTask1.getId());
        manager.getSubTask(subTask2.getId());

        final List<Task> history = manager.getHistory();

        assertEquals(3, history.size(), "Неверное количество задач в истории");

        manager.deleteEpicInId(epic.getId());

        final List<Epic> epicsAfterDeletion = manager.getAllEpics();
        final List<SubTask> subTasksDeletion = manager.getAllSubTasks();
        final List<Task> historyAfterDeletion = manager.getHistory();
        final List<Task> sortedTasksAfterDeletion = manager.getPrioritizedTasks();

        final boolean deleteEpic = epicsAfterDeletion.contains(epic);
        final boolean deleteSubTask1 = subTasksDeletion.contains(subTask1);
        final boolean deleteSubTask2 = subTasksDeletion.contains(subTask2);

        assertFalse(deleteSubTask1, "Саб таск1 не удален из списка задач");
        assertFalse(deleteSubTask2, "Саб таск2 не удален из списка задач");
        assertFalse(deleteEpic, "Эпик не удален из списка задач");
        assertEquals(0, subTasksDeletion.size(), "Неверное количество задач в списке задач");
        assertEquals(0, historyAfterDeletion.size(), "Неверное количество задач в истории просмотров");
        assertEquals(0, sortedTasksAfterDeletion.size(),
                "Неверное количество задач в отсортированном списке");

    }

    @Test
    public void deleteSubTaskInId() {
        epic = manager.create(UtilityClassForTests.epic1());
        subTask1 = manager.create(UtilityClassForTests.subTask1(epic));
        final List<Epic> epics = manager.getAllEpics();
        final List<SubTask> subTasks = manager.getAllSubTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(1, epics.size(), "Неправильное количество в списке Эпиков'");
        assertEquals(1, subTasks.size(), "Неправильное количество в списке Саб тасков'");
        assertEquals(1, sortedTasks.size(), "Неверное количество в списке отсортированных задач");

        manager.getEpic(epic.getId());
        manager.getSubTask(subTask1.getId());

        final List<Task> history = manager.getHistory();

        assertEquals(2, history.size(), "Неверное количество задач в истории");

        manager.deleteSubTaskInId(subTask1.getId());

        final List<Epic> epicsAfterDeletion = manager.getAllEpics();
        final List<SubTask> subTasksDeletion = manager.getAllSubTasks();
        final List<Task> historyAfterDeletion = manager.getHistory();
        final List<Task> sortedTasksAfterDeletion = manager.getPrioritizedTasks();

        final boolean deleteEpic = epicsAfterDeletion.contains(epic);
        final boolean deleteSubTask1 = subTasksDeletion.contains(subTask1);

        assertFalse(deleteSubTask1, "Саб таск1 не удален из списка задач");
        assertTrue(deleteEpic, "Эпик удален из списка задач");
        assertEquals(0, subTasksDeletion.size(), "Неверное количество задач в списке задач");
        assertEquals(1, historyAfterDeletion.size(), "Неверное количество задач в истории просмотров");
        assertEquals(0, sortedTasksAfterDeletion.size(),
                "Неверное количество задач в отсортированном списке");

    }

    @Test
    public void updateTask() {
        task = UtilityClassForTests.task1();
        final List<Task> tasks = manager.getAllTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(0, tasks.size(), "Список задач не пуст");
        assertEquals(0, sortedTasks.size(), "Отсортированный список задач не пуст");

        manager.update(task);

        final List<Task> tasksUpdate = manager.getAllTasks();
        final List<Task> sortedTasksUpdate = manager.getPrioritizedTasks();

        assertEquals(1, tasksUpdate.size(), "Задача не добавилась в список задач");
        assertEquals(1, sortedTasksUpdate.size(),
                "Задача не добавилась в отсортированный список задач");
    }

    @Test
    public void updateEpic() {
        epic = UtilityClassForTests.epic1();
        final List<Epic> epics = manager.getAllEpics();

        assertEquals(0, epics.size(), "Список Эпиков не пуст");

        manager.update(epic);

        final List<Epic> epicsUpdate = manager.getAllEpics();

        assertEquals(1, epicsUpdate.size(), "Неверное количество в списке Эпиков");
    }

    @Test
    public void updateSubTask() {
        epic = UtilityClassForTests.epic1();
        subTask1 = UtilityClassForTests.subTask1(epic);
        final List<SubTask> subTasks = manager.getAllSubTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(0, subTasks.size(), "Список Саб тасков не пуст");
        assertEquals(0, sortedTasks.size(), "Отсортированный список задач не пуст");

        manager.update(subTask1);

        final List<SubTask> subTasksUpdate = manager.getAllSubTasks();
        final List<Task> sortedTasksUpdate = manager.getPrioritizedTasks();

        assertEquals(1, subTasksUpdate.size(), "Неверное количество в списке Саб тасков");
        assertEquals(1, sortedTasksUpdate.size(), "Неверное количество в отсортированном списке");

    }

    @Test
    public void getHistoryTest() {
        task = manager.create(UtilityClassForTests.task1());

        manager.getTask(task.getId());

        List<Task> history = manager.getHistory();

        assertEquals(1, history.size(), "Неверный размер истории");
        assertEquals(task, history.get(0), "Задачи не совпадают");
    }

    @Test
    public void getPrioritizedTasksTest() {
        task = manager.create(UtilityClassForTests.task1());

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size(), "Неверный размер отсортированных задач");
        assertEquals(task, prioritizedTasks.get(0), "Задачи не совпадают");
    }
}