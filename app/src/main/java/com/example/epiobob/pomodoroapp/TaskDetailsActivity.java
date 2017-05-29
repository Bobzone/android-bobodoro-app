package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

/**
 * Created by epiobob on 2017-05-21.
 */

public class TaskDetailsActivity extends Activity {

    private Intent intent;

    public TaskDetailsActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        intent = getIntent();

        passDataFromIntent();
    }

    private void passDataFromIntent() {
        EditText taskDetailsTitle = (EditText) findViewById(R.id.etTaskDetailsTitle);

        Task task_context = (Task) intent.getSerializableExtra("task_context");

        taskDetailsTitle.setText(task_context.getTitle());
    }


    public void startSession(View view) {
        Intent intent = new Intent(this, SessionStartedActivity.class);
        startActivity(intent);
    }
}
