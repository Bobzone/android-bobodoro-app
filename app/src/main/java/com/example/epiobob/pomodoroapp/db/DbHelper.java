package com.example.epiobob.pomodoroapp.db;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.TaskAdapter;

import java.util.List;

/**
 * Created by epiobob on 2018-02-26.
 */

public interface DbHelper {

    String DB_NAME = "TasksDB";
    int VERSION = 1;

    String TABLE_NAME = "tasks";
    String _ID = "_id";
    String TITLE = "title";
    String DESCRIPTION = "description";
    String STATUS = "status";
    String[] columns = {_ID, TITLE, DESCRIPTION, STATUS};

    SQLiteDatabase getWritableDatabase();

    String getDatabaseName();

    void addNew(Task task);

    List<Task> getAll();

    void setOperatingDatabase(SQLiteDatabase db);

    void setAdapter(TaskAdapter adapter);
}
