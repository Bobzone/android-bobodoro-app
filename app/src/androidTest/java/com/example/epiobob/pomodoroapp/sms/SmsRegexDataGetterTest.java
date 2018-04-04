package com.example.epiobob.pomodoroapp.sms;

import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.TaskStatusEnum;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by piotr.gawronski2 on 30.03.2018.
 */
public class SmsRegexDataGetterTest {

    SmsRegexDataGetter smsRegexDataGetter;

    @Before
    public void setup() {
        smsRegexDataGetter = new SmsRegexDataGetter();
    }

    @Test
    public void shouldReturnTaskWithTitle() {
        String expectedTitle = "Very important title";
        String inputData = "This is bobodoro task. Title:" + expectedTitle + ";";

        Task task = smsRegexDataGetter.get(inputData);

        assertEquals(expectedTitle, task.getTitle());
    }

    @Test
    public void shouldReturnTaskWithTitleAndWithDescription() {
        String expectedTitle = "Very important title";
        String expectedDescription = "Also very important description";
        String inputData = "This is bobodoro task. Title:" + expectedTitle + ";" + "Description:" + expectedDescription + ";";

        Task task = smsRegexDataGetter.get(inputData);

        assertEquals("Title is wrong.", expectedTitle, task.getTitle());
        assertEquals("Desc is wrong.", expectedDescription, task.getDescription());
    }

    @Test
    public void shouldReturnTaskWithTitleAndWithDescriptionAndStatus() {
        String expectedTitle = "Very important title";
        String expectedDescription = "Also very important description";
        String expectedStatus = "IN_PROGRESS";

        String inputData = "This is bobodoro task. Title:" + expectedTitle + ";"
                + "Description:" + expectedDescription + ";"
                + "Status:" + expectedStatus + ";";

        Task task = smsRegexDataGetter.get(inputData);

        assertEquals("Title is wrong.", expectedTitle, task.getTitle());
        assertEquals("Desc is wrong.", expectedDescription, task.getDescription());
        assertEquals("Status is wrong.", TaskStatusEnum.IN_PROGRESS, task.getStatus());
    }


}