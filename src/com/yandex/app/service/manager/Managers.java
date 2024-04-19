package com.yandex.app.service.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.yandex.app.model.LocalDateAdapter;
import com.yandex.app.service.history.HistoryManager;
import com.yandex.app.service.history.InMemoryHistoryManager;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Managers {
    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }
}
