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
    private List<DataHomeBoard> dataHomeBoards;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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
        adapter = new HomeRecyclerViewAdapter(dataHomeBoards);
        recyclerView.setAdapter(adapter);

        // add test_data
        List<String> names, datas, names2, datas2;
        names = new ArrayList<>();
        names.add("Free Board");
        names.add("Secret Board");
        names.add("Issue Board");
        names.add("Market Board");
        names.add("Info Board");
        datas = new ArrayList<>();
        datas.add("What's the lateness check criterion?");
        datas.add("My Part-time paycheck changed");
        datas.add("It's the same as Yoon Suk Yeol or Moon Jae In");
        datas.add("2023 Pacific National Poetry Book Conceptual Book");
        datas.add("Guide to the Seoul Metropolitan Government's Student Loan Interest Support Project in the First Half of 2020");

        names2 = new ArrayList<>();
        names2.add("자유게시판");
        names2.add("비밀게시판");
        names2.add("이슈게시판");
        names2.add("장터게시판");
        names2.add("정보게시판");
        datas2 = new ArrayList<>();
        datas2.add("What's the lateness check criterion?");
        datas2.add("My Part-time paycheck changed");
        datas2.add("It's the same as Yoon Suk Yeol or Moon Jae In");
        datas2.add("2023 Pacific National Poetry Book Conceptual Book");
        datas2.add("Guide to the Seoul Metropolitan Government's Student Loan Interest Support Project in the First Half of 2020");


        DataHomeBoard data1 = new DataHomeBoard("Favorites",names,datas);
        DataHomeBoard data2 = new DataHomeBoard("즐겨찾는 게시판",names2,datas2);
        DataHomeBoard data3 = new DataHomeBoard("Favorites",names,datas);
        DataHomeBoard data4 = new DataHomeBoard("즐겨찾는 게시판",names2,datas2);

        dataHomeBoards.add(data1);
        dataHomeBoards.add(data2);
        dataHomeBoards.add(data3);
        dataHomeBoards.add(data4);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_home, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                // "notification" 아이템이 클릭되었을 때 수행할 코드 작성
                Snackbar.make(view, "notification", 100).show();
                Intent intent = new Intent(getContext(),NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                // "search" 아이템이 클릭되었을 때 수행할 코드 작성
                Snackbar.make(view, "search", 100).show();
                Intent intent1 = new Intent(getContext(), SearchActivity.class);
                startActivity(intent1);
                return true;
            case R.id.myPage:
                // "myPage" 아이템이 클릭되었을 때 수행할 코드 작성
                Snackbar.make(view, "mypage", 100).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
