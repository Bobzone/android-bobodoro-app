package com.example.epiobob.pomodoroapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.epiobob.pomodoroapp.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by epiobob on 2018-02-26.
 */

public class InMemoryDbHelper extends SQLiteOpenHelper implements DbHelper {

    private static final String TAG = InMemoryDbHelper.class.getSimpleName();

    private Context context;

    private Map<Long, Task> database

    public InMemoryDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    public InMemoryDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating in memory db for tasks now. ");
        database = new HashMap<>();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
