package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public List<Task> browsingHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
        if (browsingHistory.size() > 10) {
            browsingHistory.remove(0);
        }

    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }

}
