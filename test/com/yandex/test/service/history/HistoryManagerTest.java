package com.yandex.test.service.history;

import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.history.InMemoryHistoryManager;
import com.yandex.test.model.UtilityClassForTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HistoryManagerTest {

    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = UtilityClassForTests.task1();
    }

    @Test
    public void addTest() {
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        Assertions.assertNotNull(history, "Неправильный размер истории");
        Assertions.assertEquals(1, history.size(), "Неправильный размер истории");
    }

    @Test
    public void removeTest() {
        final Task task = new Task("Таск1", "Описание", 0, null);
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        Assertions.assertNotNull(history, "Неправильный размер истории");
        Assertions.assertEquals(1, history.size(), "Неправильный размер истории");

        historyManager.remove(task.getId());

        final List<Task> historyClear = historyManager.getHistory();

        Assertions.assertEquals(0, historyClear.size(), "История не пустая.");
    }

    @Test
    public void getHistoryTest() {
        List<Task> historyClear = historyManager.getHistory();

        Assertions.assertEquals(0, historyClear.size(), "История не пуста");

        historyManager.add(task);
        historyManager.add(task);

        List<Task> historyDouble = historyManager.getHistory();

        Assertions.assertEquals(1, historyDouble.size(), "Неверный размер истории");
    }
}
