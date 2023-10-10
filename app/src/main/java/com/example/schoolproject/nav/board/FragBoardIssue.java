package com.example.schoolproject.nav.board;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;
import com.example.schoolproject.post.PostWriteActivity;
import com.example.schoolproject.R;
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
    private SharedPreferences sPrefs;
    private Long curUserId;

    public static FragBoardIssue newInstance(){
        FragBoardIssue fragBoardIssue = new FragBoardIssue();
        return fragBoardIssue;
    }

    @Override
    public void onResume() {
        super.onResume();
        sPrefs = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        curUserId = sPrefs.getLong("id", -1);
        boolean isCertified = sPrefs.getBoolean("isCertified", false);
        if (!isCertified){
            // 학생증 인증하지 않으면 에러 표시
            LinearLayout linearLayout = view.findViewById(R.id.warning_issue);
            linearLayout.setVisibility(View.VISIBLE);
            FloatingActionButton fab = view.findViewById(R.id.fab_write);
            fab.setVisibility(View.GONE);
        } else {
            // get updated board
            BoardApiService apiService = new BoardApiService();
            Call<List<Board>> call = apiService.getBoardsByBoardKindMember(BoardKind.ISSUE, curUserId);
            call.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 현재 접속자 ID 가져오기
        sPrefs = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        curUserId = sPrefs.getLong("id", -1);
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


        // get posts matching boardKind
//        BoardApiService apiService = new BoardApiService();
//        Call<List<Board>> call = apiService.getBoardsByBoardKindMember(BoardKind.ISSUE, curUserId);
//        call.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter));



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