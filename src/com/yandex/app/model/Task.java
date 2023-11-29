package com.yandex.app.model;

public class Task {

    protected String[] statuses = {
            "NEW","IN_PROGRESS","DONE"};
    protected String name;
    protected String description;
    protected String status;
    protected int id;



    Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = statuses[0];
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    //protected String[] STATUSES = {"NEW", "IN_PROGRESS", "DONE"};
   // public String getSTATUSES(String item) {
   //     STATUSES statuses = STATUSES.item;
   //     return
   // }

}


