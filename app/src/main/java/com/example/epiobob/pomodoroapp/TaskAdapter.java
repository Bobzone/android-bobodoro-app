package com.example.epiobob.pomodoroapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by epiobob on 2017-05-21.
 */

class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(@NonNull Context context, List<Task> tasks) {
        super(context, R.layout.task_on_list, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.task_on_list, parent, false);

        Task singleTaskItem = getItem(position);
        TextView taskText = (TextView) customView.findViewById(R.id.etTaskItem);
        ImageView taskIcon = (ImageView) customView.findViewById(R.id.ivTaskIcon);

        taskText.setText(singleTaskItem.getTitle());
        taskIcon.setImageResource(R.drawable.tomato64);

        return customView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
