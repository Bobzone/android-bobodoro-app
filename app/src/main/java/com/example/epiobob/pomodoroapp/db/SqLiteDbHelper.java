package com.example.epiobob.pomodoroapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.TaskAdapter;
import com.example.epiobob.pomodoroapp.TaskStatusEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by epiobob on 2018-02-26.
 */

public class SqLiteDbHelper extends SQLiteOpenHelper implements DbHelper {

    private static final String TAG = SqLiteDbHelper.class.getSimpleName();
    private static SqLiteDbHelper instance = null;
    private SQLiteDatabase db;
    private TaskAdapter adapter;

    public SqLiteDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static SqLiteDbHelper getDatabase(Context context) {
        if (instance == null) {
            instance = new SqLiteDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating tasks db now. ");
        this.db = db;
        this.db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + DESCRIPTION + " TEXT NOT NULL, "
                + STATUS + " TEXT NOT NULL); ");
        initDbWithStartData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not yet implemented. ");
    }

    @Override
    public void addNew(Task task) {
        new addNewTaskAsync().execute(task);
    }

    @Override
    public List<Task> getAll() {
        List<Task> resultSet = new ArrayList<>();
        Cursor query = db.query(TABLE_NAME, columns, null, null, null, null, null);

        query.moveToFirst();
        while (!query.isAfterLast()) {
            String title = query.getString(query.getColumnIndexOrThrow(TITLE));
            String description = query.getString(query.getColumnIndexOrThrow(DESCRIPTION));
            String status = query.getString(query.getColumnIndexOrThrow(STATUS));
            resultSet.add(new Task.Builder()
                    .setTitle(title)
                    .setDescription(description)
                    .setStatus(Enum.valueOf(TaskStatusEnum.class, status))
                    .build());

            query.moveToNext();
        }

        return resultSet;
    }

    @Override
    public void setOperatingDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    private void initDbWithStartData() {
        addNew(new Task.Builder()
                .setTitle("Example Task 1")
                .setDescription("This is an example task!")
                .build());

        addNew(new Task.Builder()
                .setTitle("Example Task 2")
                .setDescription("To start Pomodoro session for this task tap here and then start the timer with the timer button.")
                .build());

        addNew(new Task.Builder()
                .setTitle("Example Task 3")
                .setDescription("You can mark these tasks as complete, delete them or edit them for further use! Good luck!")
                .build());
    }

    public void setAdapter(TaskAdapter adapter) {
        this.adapter = adapter;
    }

    private class addNewTaskAsync extends AsyncTask<Task, Void, Void> {
        @Override
        protected Void doInBackground(Task... tasks) {
            ContentValues cv = new ContentValues();
            cv.put(columns[1], tasks[0].getTitle());
            cv.put(columns[2], tasks[0].getDescription());
            cv.put(columns[3], tasks[0].getStatus().name());

            Log.d(TAG, "Inserting new record to database: " + tasks[0]);
            db.insert(TABLE_NAME, null, cv);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.updateTasks(getAll());
        }
    }

}
