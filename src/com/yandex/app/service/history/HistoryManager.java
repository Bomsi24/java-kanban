package com.yandex.app.service.history;

import com.yandex.app.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
