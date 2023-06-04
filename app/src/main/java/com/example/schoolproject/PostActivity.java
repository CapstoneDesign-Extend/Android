package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private int postId;
    private String boardName;
    private TextView tv_boardName;
    private DataBaseHelper dbHelper;
    private List<Object> dataPosts;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getIntent().getIntExtra("postId",-1);
        boardName = getIntent().getStringExtra("boardName");

        if (postId == -1){
            Toast.makeText(getApplicationContext(), "Fatal Error: postId is null", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_post);
        // set boardName
        tv_boardName = findViewById(R.id.tv_post_board_name);
        tv_boardName.setText(boardName);
        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_signUp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setting RecyclerView
        recyclerView = findViewById(R.id.recyclerview_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dataPosts = new ArrayList<>(); // initialize empty data
        adapter = new PostRecyclerViewAdapter(dataPosts);
        recyclerView.setAdapter(adapter);

        dbHelper = new DataBaseHelper(this);
        DataPost data = dbHelper.getPostData(postId);
        dataPosts.add(data);

        adapter.notifyDataSetChanged();

    }
}