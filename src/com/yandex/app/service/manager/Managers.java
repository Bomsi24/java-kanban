package com.yandex.app.service.manager;

import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.history.InMemoryHistoryManager;
import com.yandex.app.service.manager.InMemoryTaskManager;
import com.yandex.app.service.manager.TaskManager;

public class Managers {
    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
