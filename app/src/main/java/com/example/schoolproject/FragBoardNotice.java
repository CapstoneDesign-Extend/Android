package com.example.schoolproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragBoardNotice extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static FragBoardNotice newInstance(){
        FragBoardNotice fragBoardNotice = new FragBoardNotice();
        return fragBoardNotice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board_notice, container, false);

        // setting RecylerView
        recyclerView = view.findViewById(R.id.recycler_view_board_notice);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BoardNoticeRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }




}