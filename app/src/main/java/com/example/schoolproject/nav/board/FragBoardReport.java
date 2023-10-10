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

public class FragBoardReport extends Fragment {

    private View view;
    private List<Object> dataFragBoardReports;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;
    private SharedPreferences sPrefs;
    private Long curUserId;


    public static FragBoardReport newInstance(){
        FragBoardReport fragBoardReport = new FragBoardReport();
        return fragBoardReport;
    }

    @Override
    public void onResume() {
        super.onResume();
        sPrefs = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        curUserId = sPrefs.getLong("id", -1);
        // get updated board
        boolean isCertified = sPrefs.getBoolean("isCertified", false);
        if (!isCertified){
            // 학생증 인증하지 않으면 에러 표시
            LinearLayout linearLayout = view.findViewById(R.id.warning_report);
            linearLayout.setVisibility(View.VISIBLE);
            FloatingActionButton fab = view.findViewById(R.id.fab_write);
            fab.setVisibility(View.GONE);
        } else {
            // get updated board
            BoardApiService apiService = new BoardApiService();
            Call<List<Board>> call = apiService.getBoardsByBoardKindMember(BoardKind.REPORT, curUserId);
            call.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board_report, container, false);
        sPrefs = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        curUserId = sPrefs.getLong("id", -1);
        // connecting resources
        fab = view.findViewById(R.id.fab_write);
        // setting RecylerView
        recyclerView = view.findViewById(R.id.recycler_view_board_report);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dataFragBoardReports = new ArrayList<>();
        adapter = new PostPreviewRecyclerViewAdapter(getContext(), dataFragBoardReports);
        recyclerView.setAdapter(adapter);
        // get posts matching boardKind
//        BoardApiService apiService = new BoardApiService();
//        Call<List<Board>> call = apiService.getBoardsByBoardKindMember(BoardKind.REPORT, curUserId);
//        call.enqueue(new BoardCallback.BoardListCallBack(getActivity().getApplicationContext(), adapter));

        // setting listeners
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PostWriteActivity.class);
                intent.putExtra("boardKind", "REPORT");
                startActivity(intent);
            }
        });

        return view;
    }
}