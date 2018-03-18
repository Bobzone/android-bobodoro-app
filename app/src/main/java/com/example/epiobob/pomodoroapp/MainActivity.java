package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.epiobob.pomodoroapp.db.DbHelper;
import com.example.epiobob.pomodoroapp.db.SqLiteDbHelper;

import java.io.File;
import java.util.List;

import static com.example.epiobob.pomodoroapp.ResultCodes.REMOVE_TASK;
import static com.example.epiobob.pomodoroapp.ResultCodes.SAVE_TASK_CHANGE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Task taskContext;
    private List<Task> tasks;
    private TaskAdapter myAdapter;

    private static final String INTERNAL_STORAGE_FILENAME = "bobodoroTasks.dat";
    private static final File INTERNAL_STORAGE_FILE = new File(INTERNAL_STORAGE_FILENAME);
    private FloatingActionButton mainFab;
    private FloatingActionButton addTaskFab;

    private SQLiteDatabase sqLiteDatabase = null;
    private DbHelper dbHelper = null;

    private boolean mainFabClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (INTERNAL_STORAGE_FILE.exists()) {
//            readFromInternalStorage(INTERNAL_STORAGE_FILE);
//        } else {
//            initStartingTasks();
//            saveToInternalStorage(INTERNAL_STORAGE_FILE);
//        }

        mainFab = (FloatingActionButton) findViewById(R.id.fab);
        addTaskFab = (FloatingActionButton) findViewById(R.id.fab2);

        dbHelper = new SqLiteDbHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Log.d(TAG, "Database " + dbHelper.getDatabaseName() + " wired to main activity. ");

        tasks = dbHelper.getAll(sqLiteDatabase);
        myAdapter = new TaskAdapter(this, tasks);

        ListView rootListView = (ListView) findViewById(R.id.rootListView);
        rootListView.setAdapter(myAdapter);

        rootListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                        taskContext = (Task) parent.getItemAtPosition(position);
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
                if (resultCode == Activity.RESULT_OK || resultCode == ResultCodes.MASK_AS_COMPLETE_TASK) {
                    Task resultTask = (Task) data.getSerializableExtra("task_context");
                    tasks.set(tasks.indexOf(taskContext), resultTask);
                }
                if (resultCode == REMOVE_TASK) {
                    tasks.remove(taskContext);
                }
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        saveToInternalStorage(INTERNAL_STORAGE_FILE);
        myAdapter.notifyDataSetChanged();
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

    public void goToCredits(MenuItem item) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    public void mainFabClicked(View view) {
        if (!mainFabClicked) {
            Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.add_fab_show);

            View addTaskFabHint = findViewById(R.id.fab2_hint);


            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) addTaskFab.getLayoutParams();
            layoutParams.bottomMargin += (int) (addTaskFab.getHeight() * 1.4);
            addTaskFab.setLayoutParams(layoutParams);
            addTaskFab.startAnimation(show_fab_1);
            addTaskFab.setClickable(true);

            RelativeLayout.LayoutParams layoutParamsHint = (RelativeLayout.LayoutParams) addTaskFabHint.getLayoutParams();
            layoutParamsHint.bottomMargin += (int) (addTaskFab.getHeight() * 0.20);
            addTaskFabHint.setLayoutParams(layoutParamsHint);
            addTaskFabHint.startAnimation(show_fab_1);
            Log.i("MainActivity", "Creating new task...");

            mainFabClicked = !mainFabClicked;
        } else {
            // fold the buttons back
        }
    }
}
