package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SAVE_TASK_CHANGE = 113;
    private Task taskContext;
    private List<Task> tasks;
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
            tasks = new ArrayList<>();
            tasks.add(new Task.Builder().build());
//            saveToInternalStorage(INTERNAL_STORAGE_FILE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (SAVE_TASK_CHANGE): {
                if (resultCode == Activity.RESULT_OK) {
                    Task resultTask = (Task) data.getSerializableExtra("task_context");
                    tasks.set(tasks.indexOf(taskContext), resultTask);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveToInternalStorage(INTERNAL_STORAGE_FILE);
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
            tasks = (List<Task>) ois.readObject();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
