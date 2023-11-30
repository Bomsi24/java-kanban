package com.yandex.app.model;

public class SubTask extends Task {
    private final Epic epic;

    public SubTask(String name, String description,String status,int id,Epic epic) {
        super(name, description,status,id);
        this.epic = epic;

    }

    public SubTask(String name, String description, Epic epic){
        super(name,description);
        this.epic = epic;
    }

    public int getEpicId() {
        return epic.id;
    }
}
