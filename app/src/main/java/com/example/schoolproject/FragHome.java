package com.example.schoolproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FragHome extends Fragment {
    private View view;
    private List<Object> dataHomeBoards;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onResume() {
        super.onResume();

        // add testData to ViewHolder1 (DynamicMorning) ::
        String MorningTitle, MorningLecture1, MorningLecture2;
        MorningTitle = "오늘은 강의가 2개 있어요.";
        MorningLecture1 = "09:00 캡스톤디자인";
        MorningLecture2 = "13:00 모바일프로그래밍";

        DataHomeDynamicMorning data0 = new DataHomeDynamicMorning(MorningTitle, MorningLecture1, MorningLecture2);

        // Load data and set adapter
        dataHomeBoards.clear();
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        DataHomeBoard data1 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "자유게시판", "2");
        DataHomeBoard data2 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "졸업생게시판", "2");
        DataHomeBoard data3 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "새내기게시판", "2");
        DataHomeBoard data4 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "정보게시판", "2");
        DataHomeBoard data5 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "취업·진로", "2");

        dataHomeBoards.add(data0);
        dataHomeBoards.add(data1);
        dataHomeBoards.add(data2);
        dataHomeBoards.add(data3);
        dataHomeBoards.add(data4);
        dataHomeBoards.add(data5);

        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);
        setHasOptionsMenu(true);

        // setting ToolBar
        Toolbar toolbar = view.findViewById(R.id.toolbar_home);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setting RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_home);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dataHomeBoards = new ArrayList<>();  // empty data
        adapter = new HomeRecyclerViewAdapter(getContext(), dataHomeBoards);
        recyclerView.setAdapter(adapter);

        // add testData to ViewHolder1 (DynamicMorning) ::
        String MorningTitle, MorningLecture1, MorningLecture2;
        MorningTitle = "오늘은 강의가 2개 있어요.";
        MorningLecture1 = "09:00 캡스톤디자인";
        MorningLecture2 = "13:00 모바일프로그래밍";

        DataHomeDynamicMorning data0 = new DataHomeDynamicMorning(MorningTitle, MorningLecture1, MorningLecture2);
        dataHomeBoards.add(data0);


        // set data from DB to ViewHolder2 ::
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());

        DataHomeBoard data1 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "자유게시판", "2");
        DataHomeBoard data2 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "졸업생게시판", "2");
        DataHomeBoard data3 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "새내기게시판", "2");
        DataHomeBoard data4 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "정보게시판", "2");
        DataHomeBoard data5 = (DataHomeBoard) dbHelper.getHomeBoardData("Post", "취업·진로", "2");

        dataHomeBoards.add(data1);
        dataHomeBoards.add(data2);
        dataHomeBoards.add(data3);
        dataHomeBoards.add(data4);
        dataHomeBoards.add(data5);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_home, menu);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                // "notification" 아이템이 클릭되었을 때 수행할 코드 작성
                Intent intent = new Intent(getContext(),NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                // "search" 아이템이 클릭되었을 때 수행할 코드 작성
                Intent intent1 = new Intent(getContext(), SearchActivity.class);
                startActivity(intent1);
                return true;
            case R.id.myPage:
                // "myPage" 아이템이 클릭되었을 때 수행할 코드 작성
                Intent intent2 = new Intent(getContext(), MyPageActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
