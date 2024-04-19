package com.yandex.app.service.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.model.LocalDateTimeAdapter;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.history.InMemoryHistoryManager;

import java.time.LocalDateTime;

public class Managers {
    private Managers() {

    }

    public static TaskManager getDefault()  {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager("save.csv");
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
