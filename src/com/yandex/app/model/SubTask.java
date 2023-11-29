package com.yandex.app.model;

public class SubTask extends Task {
    private final int idEpic;

    SubTask(String name, String description, Epic epic) {
        super(name, description);
        idEpic = epic.id;

    }


    public int getId() {
        return id;
    }

    public int getEpicId() {
        return idEpic;
    }
}
