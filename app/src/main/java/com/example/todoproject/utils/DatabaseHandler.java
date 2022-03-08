package com.example.todoproject.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoproject.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "todoAppDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER)";
    private static SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getReadableDatabase();
    }

    public void AddNewTask(TaskModel task) {
        ContentValues values = new ContentValues();
        values.put(TASK, task.getTask());
        values.put(STATUS, 0);
        db.insert(TODO_TABLE, null, values);
    }

    @SuppressLint({"Range", "Recycle"})
    public List<TaskModel> getAllTask() {
        List<TaskModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(Boolean.parseBoolean(TODO_TABLE), null, null, null, null, null, null, null, null);
            if (cur != null) {
                do {
                    TaskModel task = new TaskModel();
                    task.setId(cur.getInt(cur.getColumnIndex(ID)));
                    task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    taskList.add(task);
                } while (cur.moveToNext());
            }
        } finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues values = new ContentValues();
        values.put(STATUS, status);
        db.update(TODO_TABLE, values, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues values = new ContentValues();
        values.put(TASK, task);
        db.update(TODO_TABLE, values, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }
}
