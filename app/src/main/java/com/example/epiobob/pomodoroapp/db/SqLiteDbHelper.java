package com.example.epiobob.pomodoroapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.epiobob.pomodoroapp.Task;

/**
 * Created by epiobob on 2018-02-26.
 */

public class SqLiteDbHelper extends SQLiteOpenHelper implements DbHelper {

    private static final String TAG = SqLiteDbHelper.class.getSimpleName();

    private Context context;

    public SqLiteDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
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

    @Override
    public boolean addNew(SQLiteDatabase db, Task task) {
        ContentValues cv = new ContentValues();
        cv.put(columns[1], task.getTitle());
        cv.put(columns[2], task.getDescription());
        cv.put(columns[3], task.getStatus().getText());

        Log.d(TAG, "Inserting new record to database: " + task);
        db.insert(TABLE_NAME, null, cv);
        return true;
    }
}
