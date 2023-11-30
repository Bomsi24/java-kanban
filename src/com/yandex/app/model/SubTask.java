package com.yandex.app.model;

public class SubTask extends Task {
   // private final Epic epic;
    private  final int idEpic;

    public SubTask(String name, String description,String status,int id,Epic epic) {
        super(name, description,status,id);
        this.idEpic = epic.getId();

    }

    public SubTask(String name, String description, Epic epic){
        super(name,description);
        this.idEpic = epic.getId();
    }

    public int getEpicId() {
        return idEpic;
    }
}
