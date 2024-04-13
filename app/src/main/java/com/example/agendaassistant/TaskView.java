package com.example.agendaassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class TaskView extends Fragment {

    private RecyclerView recyclerView;
    private taskDataAdapter taskDataAdapter;

    public TaskView(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_task_view, container, false);

        // intialize the recyclerView and adapter
        recyclerView = returnView.findViewById(R.id.taskRecyclerView);
        taskDataAdapter = new taskDataAdapter(
                AppData.getInstance().loadUserData(returnView.getContext()),
                recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(returnView.getContext()));
        recyclerView.setAdapter(taskDataAdapter);

        taskDataAdapter.notifyDataSetChanged();

        return returnView;

    }

    public void viewData(){



    }
}