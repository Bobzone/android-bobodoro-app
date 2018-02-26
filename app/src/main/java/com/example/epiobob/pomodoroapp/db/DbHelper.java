package com.example.epiobob.pomodoroapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.epiobob.pomodoroapp.Task;

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

    boolean addNew(SQLiteDatabase db, Task task);

    List<Task> getAll(SQLiteDatabase db);
}
