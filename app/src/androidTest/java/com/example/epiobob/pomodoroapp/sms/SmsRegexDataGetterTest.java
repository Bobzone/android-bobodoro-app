package com.example.epiobob.pomodoroapp.sms;

import com.example.epiobob.pomodoroapp.Task;

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
        String inputData = "This is bobodoro task. Title:" + expectedTitle;

        Task task = smsRegexDataGetter.get(inputData);

        assertEquals(expectedTitle, task.getTitle());
    }

}