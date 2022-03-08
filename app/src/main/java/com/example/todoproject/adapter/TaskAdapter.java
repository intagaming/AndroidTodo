package com.example.todoproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoproject.AddNewTask;
import com.example.todoproject.MainActivity;
import com.example.todoproject.R;
import com.example.todoproject.model.TaskModel;
import com.example.todoproject.utils.DatabaseHandler;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<TaskModel> taskList;
    private final MainActivity activity;
    private final DatabaseHandler db;

    public TaskAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder((itemView));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        TaskModel item = taskList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBool(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount() {
        return taskList.size();
    }

    private boolean toBool(int n) {
        return n != 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTask(List<TaskModel> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        TaskModel item = taskList.get(position);
        db.deleteTask(item.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        TaskModel item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
