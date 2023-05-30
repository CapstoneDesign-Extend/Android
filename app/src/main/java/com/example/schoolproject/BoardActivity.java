package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private TextView tv_boardName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Object> dataPostPreviews;
    String receivedBoardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        receivedBoardName = getIntent().getStringExtra("boardName");
        tv_boardName = findViewById(R.id.tv_board_name);
        tv_boardName.setText(receivedBoardName);

        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setting RecyclerView
        recyclerView = findViewById(R.id.recycler_view_board);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataPostPreviews = new ArrayList<>();
        adapter = new PostPreviewRecyclerViewAdapter(this, dataPostPreviews);
        recyclerView.setAdapter(adapter);

        // add testData:

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Snackbar.make(getWindow().getDecorView(), "search",100).show();
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}