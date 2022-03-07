package com.example.todoproject.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoproject.MainActivity;
import com.example.todoproject.R;
import com.example.todoproject.model.TaskModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<TaskModel> taskList;
    private final MainActivity activity;

    public TaskAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder((itemView));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskModel item = taskList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBool(item.getStatus()));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
