package com.example.epiobob.pomodoroapp;

import java.io.Serializable;

import static com.example.epiobob.pomodoroapp.TaskStatusEnum.BLANK;
import static com.example.epiobob.pomodoroapp.TaskStatusEnum.IN_PROGRESS;

/**
 * Created by epiobob on 2017-05-21.
 */

public class Task implements Serializable {

    private String title;
    private String description;
    private TaskStatusEnum status;

    private Task(String title, String description, TaskStatusEnum status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    static class Builder {
        private String title = "New task...";
        private String description = "New description...";
        private TaskStatusEnum status = IN_PROGRESS;

        public Builder() {
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setStatus(TaskStatusEnum status) {
            this.status = status;
            return this;
        }

        public Task build() {
            return new Task(title, description, status);
        }
    }

    public String getTitle() {
        if (title == null) {
            return "Empty title";
        }
        return title;
    }

    public String getDescription() {
        if (description == null) {
            return "Empty description";
        }
        return description;
    }

    public TaskStatusEnum getStatus() {
        if (status == null) {
            return BLANK;
        }
        return status;
    }
}
