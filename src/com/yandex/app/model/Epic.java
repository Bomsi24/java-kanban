package com.yandex.app.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void setSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void deleteElementSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    public void clearSubTask() {
        subTasks.clear();
    }

}

