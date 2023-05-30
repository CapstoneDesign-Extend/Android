package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class BoardActivity extends AppCompatActivity {
    private TextView tv_boardName;
    String receivedBoardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        receivedBoardName = getIntent().getStringExtra("boardName");
        tv_boardName = findViewById(R.id.tv_board_name);
        tv_boardName.setText(receivedBoardName);
    }
}