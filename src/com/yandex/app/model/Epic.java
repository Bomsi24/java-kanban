package com.yandex.app.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description);
        this.setId(id);
    }

    public Epic(String name, String description) {
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

    public void deleteElementSubTask(Integer id) {
        subTasksId.remove(id);
    }

    public void clearSubTask() {
        subTasksId.clear();
    }

}

