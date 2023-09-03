package com.example.schoolproject.nav.board;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;
import com.example.schoolproject.post.PostWriteActivity;
import com.example.schoolproject.R;
import com.example.schoolproject.model.ui.DataPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class FragBoardIssue extends Fragment {

    private View view;
    private List<Object> dataFragBoardIssues;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;

    public static FragBoardIssue newInstance(){
        FragBoardIssue fragBoardIssue = new FragBoardIssue();
        return fragBoardIssue;
    }

    @Override
    public void onResume() {
        super.onResume();
        // get updated board
        BoardApiService apiService = new BoardApiService();
        Call<List<Board>> call = apiService.getBoardsByBoardKind(BoardKind.ISSUE);
        call.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board_issue, container, false);
        // connect resources
        fab = view.findViewById(R.id.fab_write);
        // setting RecylerView
        recyclerView = view.findViewById(R.id.recycler_view_board_notice);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dataFragBoardIssues = new ArrayList<>();
        adapter = new PostPreviewRecyclerViewAdapter(getContext(), dataFragBoardIssues);
        recyclerView.setAdapter(adapter);

        // testData
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

        // get posts matching boardKind
        BoardApiService apiService = new BoardApiService();
        Call<List<Board>> call = apiService.getBoardsByBoardKind(BoardKind.ISSUE);
        call.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter));



        // set listeners
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PostWriteActivity.class);
                intent.putExtra("boardKind","ISSUE");
                startActivity(intent);
            }
        });


        return view;
    }




}