package com.example.todoproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoproject.adapter.TaskAdapter;
import com.example.todoproject.model.TaskModel;
import com.example.todoproject.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private FloatingActionButton fab;

    private List<TaskModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<>();
        db = new DatabaseHandler(this);
        db.openDatabase();

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(db, this);
        taskRecyclerView.setAdapter(taskAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        taskList = db.getAllTask();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handlerDialogClose(DialogInterface dialog) {
        taskList = db.getAllTask();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}