package com.yandex.app.model;

public class Task {

    protected int id = -1;
    protected TypeTasks type;
    protected String name;
    protected Statuses status;
    protected String description;

    @Override
    public String toString() {
        return '\n' + Integer.toString(id) + ',' +
                type + ',' +
                name + ',' +
                status + ',' +
                description;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Statuses.NEW;
        this.type = TypeTasks.TASK;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Statuses status) { // обновление статуса
        this.status = status;
    }

    public Statuses getCurrentStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setType(TypeTasks type){
        this.type = type;
    }
    public  TypeTasks getType() {
        return type;
    }

}


