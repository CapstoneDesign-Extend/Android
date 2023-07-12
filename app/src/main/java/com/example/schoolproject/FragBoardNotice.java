package com.example.schoolproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class FragBoardNotice extends Fragment {

    private View view;
    private List<Object> dataFragBoardNotices;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;

    public static FragBoardNotice newInstance(){
        FragBoardNotice fragBoardNotice = new FragBoardNotice();
        return fragBoardNotice;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board_notice, container, false);
        // connect resources
        fab = view.findViewById(R.id.fab_write);
        // setting RecylerView
        recyclerView = view.findViewById(R.id.recycler_view_board_notice);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dataFragBoardNotices = new ArrayList<>();
        adapter = new PostPreviewRecyclerViewAdapter(getContext(), dataFragBoardNotices);
        recyclerView.setAdapter(adapter);

        // add testData
        DataPost testData1 = new DataPost(
                R.drawable.img_board_sample1,
                "서일대학교 장학금 규정 안내",
                        "그 외 장학금에 대해서 궁금 하시면\n" +
                        "(https://cafe.naver.com/seoiluniversity/826)\n" +
                        "장학금 QnA\n" +
                        "(https://cafe.naver.com/seoiluniversity/1416)",
                "서일대학교카페",
                "11:28","0","0"
                );

        dataFragBoardNotices.add(testData1);
        adapter.notifyDataSetChanged();


        // set listeners
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PostWriteActivity.class);
                intent.putExtra("boardName","notice");
                startActivity(intent);
            }
        });


        return view;
    }




}