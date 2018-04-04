package com.example.epiobob.pomodoroapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by piotr.gawronski2 on 04.04.2018.
 */
public class TaskStatusEnumTest {

    @Test
    public void testCreationFromString() {
        String str = "IN PROGRESS";
        TaskStatusEnum taskStatusEnum = TaskStatusEnum.valueOf(str);
        assertEquals(TaskStatusEnum.IN_PROGRESS, taskStatusEnum);
    }

}