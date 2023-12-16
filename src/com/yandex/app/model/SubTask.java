package com.yandex.app.model;

public class SubTask extends Task {
    private final Epic epic;

    public SubTask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Epic getEpicOfSubTask() {
        return epic;
    }
}
