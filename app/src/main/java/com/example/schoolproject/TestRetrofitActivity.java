package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class TestRetrofitActivity extends AppCompatActivity {
    TextView tv_userId, tv_id, tv_title, tv_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_retrofit);

        tv_userId = findViewById(R.id.test_tv_userId);
        tv_id = findViewById(R.id.test_tv_id);
        tv_title = findViewById(R.id.test_tv_title);
        tv_body = findViewById(R.id.test_tv_body);

        List<Post> posts = getIntent().getParcelableArrayListExtra("posts");
        if(posts != null){
            if (!posts.isEmpty()){
                tv_userId.setText(String.valueOf(posts.get(0).getUserId()));
                tv_id.setText(String.valueOf(posts.get(0).getId()));
                tv_title.setText(posts.get(0).getTitle());
                tv_body.setText(posts.get(0).getBody());
            }
        }
    }
}