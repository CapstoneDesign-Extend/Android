package com.example.schoolproject.nav.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.post.PostWriteActivity;
import com.example.schoolproject.search.SearchActivity;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;
import com.example.schoolproject.test.DataBaseHelper;
import com.example.schoolproject.model.DataPost;
import com.example.schoolproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeBoardActivity extends AppCompatActivity {
    String receivedBoardName;
    private SharedPreferences sharedPrefs;
    private List<Object> dataPosts;
    private TextView tv_boardName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;


    @Override
    protected void onResume() {
        super.onResume();
        // Load data and set adapter
        dataPosts.clear();
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        List<Object> dBData = dbHelper.getPreviewData(receivedBoardName);
        dataPosts.addAll(dBData);
        adapter.notifyDataSetChanged();
    }


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setting FloatingActionButton;
        fab = findViewById(R.id.fab_board_write);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostWriteActivity.class);
                intent.putExtra("boardName",receivedBoardName);
                startActivity(intent);
            }
        });

        // setting RecyclerView
        recyclerView = findViewById(R.id.recycler_view_board);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataPosts = new ArrayList<>();  // initialize empty data
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        List<Object> dbData = dbHelper.getPreviewData(receivedBoardName);
        dataPosts.addAll(dbData);  // add db data to list

        adapter = new PostPreviewRecyclerViewAdapter(this, dataPosts);
        recyclerView.setAdapter(adapter);


        // add testData
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



        dataPosts.add(testData1);
        adapter.notifyDataSetChanged();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
            case R.id.search:
                Snackbar.make(getWindow().getDecorView(), "search",100).show();
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
