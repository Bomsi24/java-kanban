package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected int id = -1;
    protected TypeTasks type;
    protected String name;
    protected Statuses status;
    protected String description;
    protected long duration = 0;
    protected LocalDateTime startTime;

    @Override
    public String toString() {
        String toString = '\n' + Integer.toString(id) + ',' +
                type + ',' +
                name + ',' +
                status + ',' +
                description;
        if (duration == 0) {
            return toString + ',' + duration + ',' + "null";

        } else {
            return toString + ',' + duration + ',' +
                    startTime;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && type == task.type &&
                Objects.equals(name, task.name) && status == task.status &&
                Objects.equals(description, task.description) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description, duration, startTime);
    }


    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = Statuses.NEW;
        this.type = TypeTasks.TASK;
        this.startTime = startTime;
        this.duration = duration;
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

    public void setType(TypeTasks type) {
        this.type = type;
    }

    public TypeTasks getType() {
        return type;
    }

    public LocalDateTime getStartTimeToString() {
        return startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }

    public LocalDateTime getEndTimeToString() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }

    public void createTime(long duration, LocalDateTime startTime) {
        this.duration = duration;
        this.startTime = startTime;
    }

}


