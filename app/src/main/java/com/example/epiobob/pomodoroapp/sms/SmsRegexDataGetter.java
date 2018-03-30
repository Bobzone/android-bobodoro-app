package com.example.epiobob.pomodoroapp.sms;

import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.TaskStatusEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by piotr.gawronski2 on 30.03.2018.
 */

public class SmsRegexDataGetter {

    public Task get(String source) {
        String taskTitle = null;
        String taskDescription = null;
        TaskStatusEnum taskStatus = null;

        Pattern p = Pattern.compile("Title:(.*?);");
        Matcher m = p.matcher(source);
        if (m.find()) {
            taskTitle = m.group(1); // " that is awesome"
        }

        p = Pattern.compile("Description:(.*?);");
        m = p.matcher(source);
        if (m.find()) {
            taskDescription = m.group(1); // " that is awesome"
        }

        p = Pattern.compile("Status:(.*?);");
        m = p.matcher(source);
        if (m.find()) {
            taskStatus = TaskStatusEnum.valueOf(m.group(1)); // " that is awesome"
        }

        return new Task.Builder()
                .setTitle(taskTitle)
                .setDescription(taskDescription)
                .setStatus(taskStatus)
                .build();
    }
}
