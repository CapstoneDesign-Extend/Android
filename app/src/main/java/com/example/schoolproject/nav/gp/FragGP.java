package com.example.schoolproject.nav.gp;

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

import com.example.schoolproject.R;
import com.example.schoolproject.mypage.MyPageActivity;
import com.example.schoolproject.notification.NotificationActivity;
import com.example.schoolproject.model.DataGP;
import com.example.schoolproject.model.DataTimeTable;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FragGP extends Fragment {
    private View view;
    private List<Object> dataGPs;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gp, container,false);

        setHasOptionsMenu(true);

        // setting ToolBar
        Toolbar toolbar = view.findViewById(R.id.toolbar_gp);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setting multi-view recyclerview
        recyclerView = view.findViewById(R.id.recycler_view_gp);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dataGPs = new ArrayList<>(); // initialize empty data
        adapter = new GPRecyclerViewAdapter(getContext(), dataGPs);
        recyclerView.setAdapter(adapter);
        // add temp data
        DataGP dataGP = new DataGP("4.3", "110");
        DataTimeTable dataTable = new DataTimeTable();
        dataGPs.add(dataGP);
        dataGPs.add(dataTable);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_home,menu);

        // remove searchItem
        menu.removeItem(R.id.search);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                // "notification" 아이템이 클릭되었을 때 수행할 코드 작성
                Snackbar.make(view, "notification", 100).show();
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
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
