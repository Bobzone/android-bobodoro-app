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
        String taskTitle = getRegexResult(source, "Title:(.*?);");

        String taskDescription = getRegexResult(source, "Description:(.*?);");

        String taskStatusString = getRegexResult(source, "Status:(.*?);");
        TaskStatusEnum taskStatus = TaskStatusEnum.valueOf(taskStatusString);

        return new Task.Builder()
                .setTitle(taskTitle)
                .setDescription(taskDescription)
                .setStatus(taskStatus)
                .build();
    }

    private String getRegexResult(String source, String regex) {
        String result = null;
        Pattern p;
        Matcher m;
        p = Pattern.compile(regex);
        m = p.matcher(source);
        if (m.find()) {
            result = m.group(1);
        }
        return result;
    }
}
