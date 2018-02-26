package com.example.epiobob.pomodoroapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.TaskStatusEnum;

import java.util.ArrayList;
import java.util.List;

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
                + STATUS + " TEXT NOT NULL); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Not yet implemented. ");
    }

    @Override
    public boolean addNew(SQLiteDatabase db, Task task) {
        ContentValues cv = new ContentValues();
        cv.put(columns[1], task.getTitle());
        cv.put(columns[2], task.getDescription());
        cv.put(columns[3], task.getStatus().name());

        Log.d(TAG, "Inserting new record to database: " + task);
        db.insert(TABLE_NAME, null, cv);
        return true;
    }

    @Override
    public List<Task> getAll(SQLiteDatabase db) {
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
}
