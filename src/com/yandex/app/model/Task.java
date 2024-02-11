package com.yandex.app.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Task {

    protected int id = -1;
    protected TypeTasks type;
    protected String name;
    protected Statuses status;
    protected String description;
    protected long duration = -1;
    protected LocalDateTime startTime;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    @Override
    public String toString() {
        String toString = '\n' + Integer.toString(id) + ',' +
                type + ',' +
                name + ',' +
                status + ',' +
                description;
        if (duration < 0) {
            return toString + ',' + duration + ',' + "null";

        } else {
            return toString + ',' + duration + ',' +
                    startTime.format(formatter);
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
                Objects.equals(startTime, task.startTime) && Objects.equals(formatter, task.formatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description, duration, startTime, formatter);
    }

    public Task(String name, String description, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = Statuses.NEW;
        this.type = TypeTasks.TASK;
        createTime(duration, startTime);
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

    public void setType(TypeTasks type) {
        this.type = type;
    }

    public TypeTasks getType() {
        return type;
    }

    public String getStartTimeToString() {
        return startTime.format(formatter);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public String getEndTimeToString() {
        return startTime.plusMinutes(duration).format(formatter);
    }

    public void createTime(long duration, String startTime) {
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime, formatter);


    }

}


