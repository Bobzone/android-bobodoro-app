package com.example.epiobob.pomodoroapp;

/**
 * Created by epiobob on 2017-05-28.
 */

enum TaskStatusEnum {

    COMPLETE("COMPLETE"),
    IN_PROGRESS("IN PROGRESS"),
    ARCHIVED("ARCHIVED"),
    BLANK("");

    private String text;

    TaskStatusEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
