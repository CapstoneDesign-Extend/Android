package com.example.schoolproject.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.R;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKindUtils;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.test.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PostActivity extends AppCompatActivity {
    private Long postId;
    private String boardKind;
    private TextView tv_boardName;
    private List<Object> dataPosts;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

        

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postId = getIntent().getLongExtra("postId",-1);
        boardKind = getIntent().getStringExtra("boardKind");
        if (postId == -1){
            Toast.makeText(getApplicationContext(), "Fatal Error: postId is null", Toast.LENGTH_SHORT).show();
        }


        // set boardName
        tv_boardName = findViewById(R.id.tv_post_board_name);
        tv_boardName.setText(BoardKindUtils.getBoardTitleByString(boardKind));
        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setting RecyclerView
        recyclerView = findViewById(R.id.recyclerview_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dataPosts = new ArrayList<>(); // initialize empty data
        adapter = new PostRecyclerViewAdapter(dataPosts);
        recyclerView.setAdapter(adapter);

        // get post matching postId
        BoardApiService apiService = new BoardApiService();
        Call<Board> call = apiService.getBoardById(postId);
        call.enqueue(new BoardCallback(PostActivity.this, getApplicationContext(), adapter));

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}