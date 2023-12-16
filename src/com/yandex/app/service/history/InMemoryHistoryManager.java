package com.yandex.app.service.history;

import com.yandex.app.model.Task;
import com.yandex.app.service.history.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int SIZE_HISTORY = 10;
    public final LinkedList<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
        if (browsingHistory.size() > SIZE_HISTORY) {
            browsingHistory.removeFirst();
        }

    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(browsingHistory);
    }

}
