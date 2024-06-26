package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private final Epic epic;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epic, subTask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epic);
    }

    @Override
    public String toString() {
        return super.toString() + ',' +
                epic.getId();
    }

    public SubTask(String name, String description, Epic epic, long duration, LocalDateTime time) {
        super(name, description,duration,time);
        this.epic = epic;
        this.type = TypeTasks.SUB_TASK;
    }

    public Epic getEpicOfSubTask() {
        return epic;
    }
}
