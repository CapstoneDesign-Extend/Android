package com.example.schoolproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragHome extends Fragment {
    private View view;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);


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
                Toast.makeText(getActivity(), "notification", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.search:
                // "search" 아이템이 클릭되었을 때 수행할 코드 작성
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.myPage:
                // "myPage" 아이템이 클릭되었을 때 수행할 코드 작성
                Toast.makeText(getActivity(), "mypage", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
