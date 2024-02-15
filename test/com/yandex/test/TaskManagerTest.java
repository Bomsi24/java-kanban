package com.yandex.test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.StartTimeComparator;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.manager.Managers;
import com.yandex.app.service.manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;


    @Test
    public void createTask() {

        final Task task1 = new Task("Купить молоко", "Сходить в магазин");
        final Task taskTest = manager.create(task1);

        assertNotNull(taskTest, "Задача не найдена");
        assertEquals(taskTest, task1, "Задачи не совпадают");

        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задача не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(taskTest, tasks.get(0), "Задача не совпадают");

    }

    @Test
    public void createEpic() {
        final Epic epic = new Epic("Купить молоко", "Сходить в магазин");
        final Epic epicTest = manager.create(epic);

        assertNotNull(epicTest, "Эпик не найден");
        assertEquals(epicTest, epic, "Эпик не найден");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпик не возвращается");
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epicTest, epics.get(0), "Задачи не совпадают");
    }

    @Test
    public void createSub() {
        final Epic epic = new Epic("Имя эпика", "Описание эпика");
        assertNotNull(epic, "Пустой эпик");

        final SubTask subTask1 = new SubTask("СабТаск1", "Описание", epic);
        final SubTask subTaskTest = manager.create(subTask1);

        assertNotNull(subTaskTest, "СабТаск не найден");
        assertEquals(subTaskTest, subTask1, "СабТаск не найден");

        List<SubTask> subTasks = manager.getAllSubTasks();

        assertNotNull(subTasks, "СабТаск не возвращается");
        assertEquals(1, subTasks.size(), "Неверное количество СабТасков");
        assertEquals(subTaskTest, subTasks.get(0), "СабТаски не совпадают");
    }

    @Test
    public void getAllTasks() {
        final Task task1 = manager.create(new Task("Задача1", "Описание"));
        final Task task2 = manager.create(new Task("Задача2", "Описание"));


        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Список задач пуст");
        assertEquals(2, tasks.size(), "Неверное количество задач");
    }

    @Test
    public void getAllEpics() {
        final Epic epic1 = manager.create(new Epic("Задача1", "Описание"));
        final Epic epic2 = manager.create(new Epic("Задача2", "Описание"));

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Список задач пуст");
        assertEquals(2, epics.size(), "Неверное количество задач");
    }

    @Test
    public void getAllSubTasks() {
        final Epic epic = manager.create(new Epic("Имя эпика", "Описание эпика"));
        assertNotNull(epic, "Пустой эпик");

        final SubTask subTask1 = manager.create(new SubTask("Задача1", "Описание", epic));
        final SubTask subTask2 = manager.create(new SubTask("Задача2", "Описание", epic));


        final List<SubTask> subTasks = manager.getAllSubTasks();

        assertNotNull(subTasks, "Список задач пуст");
        assertEquals(2, subTasks.size(), "Неверное количество задач");
    }

    @Test
    public void deleteTasks() {

        final Task task1 = manager.create(new Task("Задача1", "Описание"));
        final Task task2 = manager.create(new Task("Задача2", "Описание"));

        final List<Task> sortedTasks = manager.getPrioritizedTasks();
        final List<Task> tasks = manager.getAllTasks();

        manager.getTask(task1.getId());
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
        final Epic epic = manager.create(new Epic("Эпик1", "Описание"));
        final SubTask subTask1 = manager.create(new SubTask("СабТаск1", "Описание", epic));
        final SubTask subTask2 = manager.create(new SubTask("СабТаск2", "Описание", epic));

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
        final Epic epic = manager.create(new Epic("Эпик1", "Описание"));
        final SubTask subTask1 = manager.create(new SubTask("СабТаск1", "Описание", epic));
        final SubTask subTask2 = manager.create(new SubTask("СабТаск2", "Описание", epic));

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
        final Task task1 = manager.create(new Task("Задача1", "Описание"));
        final Task task2 = new Task("Задача2", "Описание");
        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "История не пуста");

        Task task1Test = manager.getTask(task1.getId());
        Task task2Test = manager.getTask(task2.getId());

        final List<Task> historyClear = manager.getHistory();

        assertEquals(task1, task1Test, "Задачи не равны");
        assertEquals(1, historyClear.size(), "Неверный размер истории");
        assertNotNull(task1Test, "Неверно возвращается задача созданная через менеджер");
        assertNull(task2Test, "Неверна возвращается задача созданный без менеджера");

    }

    @Test
    public void getEpic() {
        final Epic epic1 = manager.create(new Epic("Эпик1", "Описание"));
        Epic epic2 = new Epic("Эпик2", "Описание");
        final List<Task> history = manager.getHistory();

        assertEquals(0, history.size(), "История не пуста");

        Epic epic1Test = manager.getEpic(epic1.getId());
        Epic epic2Test = manager.getEpic(epic2.getId());

        final List<Task> historyClear = manager.getHistory();

        assertEquals(epic1, epic1Test, "Задачи не равны");
        assertEquals(1, historyClear.size(), "Неверный размер истории");
        assertNotNull(epic1Test, "Неверно возвращается Эпик созданная через менеджер");
        assertNull(epic2Test, "Неверна возвращается Эпик созданный без менеджера");

    }

    @Test
    public void getSubTask() {
        final Epic epic = manager.create(new Epic("Эпик1", "Описание"));
        final SubTask subTask1 = manager.create(new SubTask("Саб таск1", "Описание", epic));
        SubTask subTask2 = new SubTask("Саб таск2", "Описание", epic);
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
        final Task task1 = manager.create(new Task("Задача1", "Описание"));
        final Task task2 = manager.create(new Task("Задача2", "Описание"));
        final List<Task> tasks = manager.getAllTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(2, tasks.size(), "Неправильное количество в списке задач");
        assertEquals(2, sortedTasks.size(), "Неверное количество в списке отсортированных задач");

        manager.getTask(task1.getId());
        manager.getTask(task2.getId());

        final List<Task> history = manager.getHistory();

        assertEquals(2, history.size(), "Неверное количество задач в истории");

        manager.deleteTaskInId(task1.getId());

        final List<Task> tasksAfterDeletion = manager.getAllTasks();
        final List<Task> historyAfterDeletion = manager.getHistory();
        final List<Task> sortedTasksAfterDeletion = manager.getPrioritizedTasks();

        final boolean deleteTask = tasksAfterDeletion.contains(task1);

        assertFalse(deleteTask, "Задача не удалена из списка задач");
        assertEquals(1, tasksAfterDeletion.size(), "Неверное количество задач в списке задач");
        assertEquals(1, historyAfterDeletion.size(), "Неверное количество задач в истории просмотров");
        assertEquals(1, sortedTasksAfterDeletion.size(),
                "Неверное количество задач в отсортированном списке");
    }

    @Test
    public void deleteEpicInId() {
        final Epic epic = manager.create(new Epic("Эпик1", "Описание"));
        final SubTask subTask1 = manager.create(new SubTask("СабТаск1", "Описание", epic));
        final SubTask subTask2 = manager.create(new SubTask("СабТаск2", "Описание", epic));
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
        final Epic epic = manager.create(new Epic("Эпик1", "Описание"));
        final SubTask subTask1 = manager.create(new SubTask("СабТаск1", "Описание", epic));
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
        final Task task1 = new Task("Задача1", "Описание");
        final List<Task> tasks = manager.getAllTasks();
        final List<Task> sortedTasks = manager.getPrioritizedTasks();

        assertEquals(0, tasks.size(), "Список задач не пуст");
        assertEquals(0, sortedTasks.size(), "Отсортированный список задач не пуст");

        manager.update(task1);

        final List<Task> tasksUpdate = manager.getAllTasks();
        final List<Task> sortedTasksUpdate = manager.getPrioritizedTasks();

        assertEquals(1, tasksUpdate.size(), "Задача не добавилась в список задач");
        assertEquals(1, sortedTasksUpdate.size(),
                "Задача не добавилась в отсортированный список задач");
    }

    @Test
    public void updateEpic() {
        final Epic epic = new Epic("Эпик1", "Описание");
        final List<Epic> epics = manager.getAllEpics();

        assertEquals(0, epics.size(), "Список Эпиков не пуст");

        manager.update(epic);

        final List<Epic> epicsUpdate = manager.getAllEpics();

        assertEquals(1, epicsUpdate.size(), "Неверное количество в списке Эпиков");
    }

    @Test
    public void updateSubTask() {
        final Epic epic = new Epic("Эпик1", "Описание");
        final SubTask subTask1 = new SubTask("СабТаск1", "Описание", epic);
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
        final Task task1 = manager.create(new Task("Задача1", "Описание"));

        manager.getTask(task1.getId());

        List<Task> history = manager.getHistory();

        assertEquals(1, history.size(), "Неверный размер истории");
        assertEquals(task1, history.get(0), "Задачи не совпадают");
    }

    @Test
    public void getPrioritizedTasksTest() {
        final Task task1 = manager.create(new Task("Задача1", "Описание"));

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size(), "Неверный размер отсортированных задач");
        assertEquals(task1, prioritizedTasks.get(0), "Задачи не совпадают");
    }
}