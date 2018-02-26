package com.example.epiobob.pomodoroapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by epiobob on 2018-02-26.
 */

public class InMemoryDbHelper extends SQLiteOpenHelper implements DbHelper {

    private static final String TAG = InMemoryDbHelper.class.getSimpleName();
    private static final String DB_NAME = "TasksDB";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String _ID = "_id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String STATUS = "status";
    private static final String[] columns = {_ID, TITLE, DESCRIPTION, STATUS};

    private Context context;

    public InMemoryDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    public InMemoryDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating tasks db now. ");
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + DESCRIPTION + " TEXT NOT NULL, "
                + STATUS + "TEXT NOT NULL); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
