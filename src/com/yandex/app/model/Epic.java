package com.yandex.app.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    Epic(String name, String description) {
        super(name, description);
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(int index) {
        return statuses[index];
    }

    public void setSubTasks(int id) {

        subTasksId.add(id);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasksId;
    }

    public void deleteElementSubTask(int id) {
        subTasksId.remove(id);
    }


    public void clearSubTask() {
        subTasksId.clear();
    }

    public int getId() {
        return id;
    }
}

