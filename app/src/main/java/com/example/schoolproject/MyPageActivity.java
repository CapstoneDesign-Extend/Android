package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyPageActivity extends AppCompatActivity {
    private TextView tv_logout;
    private TextView test_tv_retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_myPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_logout = findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // init SharedPreferences
                SharedPreferences sharedPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                // set loginState false
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                // start new task with LoginActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(MyPageActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        test_tv_retrofit = findViewById(R.id.test_tv_retrofit);
        test_tv_retrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestRetrofit testRetrofit = new TestRetrofit(getApplicationContext());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
        }
        return super.onOptionsItemSelected(item);
    }
}