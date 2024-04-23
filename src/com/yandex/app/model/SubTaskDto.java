package com.yandex.app.model;

import java.time.LocalDateTime;

public class SubTaskDto {
    private String name;
    private String description;
    private long duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public SubTaskDto(
            String name,
            String description,
            long duration,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setTime(LocalDateTime time) {
        this.startTime = time;
    }

}