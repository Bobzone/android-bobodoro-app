package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.epiobob.pomodoroapp.db.DbHelper;
import com.example.epiobob.pomodoroapp.db.SqLiteDbHelper;

import static com.example.epiobob.pomodoroapp.ResultCodes.*;

/**
 * Created by epiobob on 2017-05-21.
 */

public class TaskDetailsActivity extends Activity {

    private Intent intent;
    private Task taskContext;
    private EditText taskDetailsTitle;
    private EditText taskDetailsDescription;
    private DbHelper dbHelper;

    public TaskDetailsActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        dbHelper = SqLiteDbHelper.getDatabase(this);
        intent = getIntent();

        passDataFromIntent();
    }

    private void passDataFromIntent() {
        taskDetailsTitle = (EditText) findViewById(R.id.etTaskDetailsTitle);
        taskDetailsDescription = (EditText) findViewById(R.id.etTaskDetailsDescription);
//        taskDetailsStatus = (EditText) findViewById(R.id.etTaskDetailsStatus);
        taskContext = (Task) intent.getSerializableExtra("task_context");

        taskDetailsTitle.setText(taskContext.getTitle());
        taskDetailsDescription.setText(taskContext.getDescription());
    }


    public void startSession(View view) {
        Intent intent = new Intent(this, SessionStartedActivity.class);
        intent.putExtra("task_context", taskContext);
        startActivity(intent);
    }

    // TODO: this method has to add new task if you are not editing
    // and update when you edit already saved task
    public void saveChanges(View view) {
        taskContext = new Task.Builder()
                .setTitle(String.valueOf(taskDetailsTitle.getText()))
                .setDescription(String.valueOf(taskDetailsDescription.getText()))
                .build();
        Log.i("TaskDetailsActivity", "Saving changes to task.");
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_context", taskContext);
        setResult(Activity.RESULT_OK, resultIntent);
        dbHelper.addNew(taskContext);
        finish();
    }

    public void removeTask(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_context", taskContext);
        setResult(REMOVE_TASK, resultIntent);
        finish();
    }

    public void markAsComplete(View view) {
        Intent resultIntent = new Intent();
        taskContext.markAsComplete();
        resultIntent.putExtra("task_context", taskContext);
        setResult(MASK_AS_COMPLETE_TASK, resultIntent);
        finish();
    }

    public void shareTask(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Your collegue shares a Bobodoro task with you. Read this message, then open the app to check it out!";
        // TODO: fix this now
//        sharingIntent.putExtra("task_context", taskContext);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        setResult(SHARE_TASK, sharingIntent);
        finish();
    }
}
