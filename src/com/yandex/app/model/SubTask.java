package com.yandex.app.model;

public class SubTask extends Task {
    private final Epic epic;

    @Override
    public String toString() {
        return super.toString() + ',' +
                epic.getId();
    }

    public SubTask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        this.type = TypeTasks.SUB_TASK;
    }

    public Epic getEpicOfSubTask() {
        return epic;
    }
}
