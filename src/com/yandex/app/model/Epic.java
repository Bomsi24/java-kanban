package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final transient List<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description,long duration,LocalDateTime time) {
        super(name, description,duration,time);
        this.type = TypeTasks.EPIC;
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;

    }


}

