package com.example.schoolproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragBoardReport extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static FragBoardReport newInstance(){
        FragBoardReport fragBoardReport = new FragBoardReport();
        return fragBoardReport;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board_report, container, false);

        // setting RecylerView
        recyclerView = view.findViewById(R.id.recycler_view_board_report);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BoardReportRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }
}