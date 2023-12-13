package com.yandex.app.model;

public class Task {


    public enum Statuses {
        NEW,
        IN_PROGRESS,
        DONE
    }
    protected String name;
    protected String description;
    protected Statuses status;
    protected int id;

    @Override
    public String toString() {
        return '\n' + "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id;
    }

    public Task(String name, String description, Statuses status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Statuses.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
}


