package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by epiobob on 2017-05-21.
 */

public class TaskDetailsActivity extends Activity {

    private Intent intent;
    private Task taskContext;
    private EditText taskDetailsTitle;
    private EditText taskDetailsDescription;

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
        taskDetailsTitle = (EditText) findViewById(R.id.etTaskDetailsTitle);
        taskDetailsDescription = (EditText) findViewById(R.id.etTaskDetailsDescription);
        taskContext = (Task) intent.getSerializableExtra("task_context");

        taskDetailsTitle.setText(taskContext.getTitle());
    }


    public void startSession(View view) {
        Intent intent = new Intent(this, SessionStartedActivity.class);
        intent.putExtra("task_context", taskContext);
        startActivity(intent);
    }

    public void saveChanges(View view) {
        taskContext = new Task.Builder()
                .setTitle(String.valueOf(taskDetailsTitle.getText()))
                .setDescription(String.valueOf(taskDetailsDescription.getText()))
                .build();
        Log.i("TaskDetailsActivity", "Saving changes!!!");
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_context", taskContext);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


}
