package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.epiobob.pomodoroapp.ResultCodes.*;

public class MainActivity extends AppCompatActivity {

    private Task taskContext;
    private ArrayList<Task> tasks;
    private TaskAdapter myAdapter;

    private static final String INTERNAL_STORAGE_FILENAME = "bobodoroTasks.dat";
    private static final File INTERNAL_STORAGE_FILE = new File(INTERNAL_STORAGE_FILENAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (INTERNAL_STORAGE_FILE.exists()) {
            readFromInternalStorage(INTERNAL_STORAGE_FILE);
        } else {
            initStartingTasks();
            saveToInternalStorage(INTERNAL_STORAGE_FILE);
        }

        myAdapter = new TaskAdapter(this, tasks);

        ListView rootListView = (ListView) findViewById(R.id.rootListView);
        rootListView.setAdapter(myAdapter);

        rootListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                        taskContext = (Task) parent.getItemAtPosition(position);
//                        TODO - this might work - refactor material
//                        intent.putExtra("task_context", tasks.get(tasks.indexOf(taskContext)));
                        intent.putExtra("task_context", taskContext);
                        startActivityForResult(intent, SAVE_TASK_CHANGE);
                    }
                }
        );
    }

    private void initStartingTasks() {
        tasks = new ArrayList<>();
        tasks.add(new Task.Builder()
                .setTitle("Example Task 1")
                .setDescription("This is an example task!")
                .build());
        tasks.add(new Task.Builder()
                .setTitle("Example Task 2")
                .setDescription("To start Pomodoro session for this task tap here and then start the timer with the timer button.")
                .build());
        tasks.add(new Task.Builder()
                .setTitle("Example Task 3")
                .setDescription("You can mark these tasks as complete, delete them or edit them for further use! Good luck!")
                .build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (SAVE_TASK_CHANGE): {
                if (resultCode == Activity.RESULT_OK || resultCode == ResultCodes.MASK_AS_COMPLETE_TASK) {
                    Task resultTask = (Task) data.getSerializableExtra("task_context");
                    tasks.set(tasks.indexOf(taskContext), resultTask);
                }
                if (resultCode == REMOVE_TASK) {
                    tasks.remove(taskContext);
                }
//                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveToInternalStorage(INTERNAL_STORAGE_FILE);
        myAdapter.notifyDataSetChanged();
    }

    public void saveToInternalStorage(final File file) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(openFileOutput(file.getName(), Context.MODE_PRIVATE));
            out.writeObject(tasks);
            Log.i("MainActivity", "Saving to internal storage...");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromInternalStorage(final File file) {
        FileInputStream fis;
        try {
            fis = openFileInput(file.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            tasks = (ArrayList<Task>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable("tasks", tasks);
//        Log.i("MSG", "saving Game");
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        tasks = (ArrayList<Task>) savedInstanceState.getSerializable("tasks");
//        myAdapter.notifyDataSetChanged();
//    }

    public void goToCredits(MenuItem item) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    public void createNewTask(View view) {
        Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
        taskContext = new Task.Builder().build();
        tasks.add(taskContext);
        intent.putExtra("task_context", taskContext);
        startActivityForResult(intent, SAVE_TASK_CHANGE);
        Log.i("MainActivity", "Creating new task...");
    }
}
