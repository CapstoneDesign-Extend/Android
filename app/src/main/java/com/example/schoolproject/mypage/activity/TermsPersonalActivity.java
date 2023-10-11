package com.example.schoolproject.mypage.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;



public class TermsPersonalActivity extends AppCompatActivity {
    com.example.schoolproject.databinding.ActivityTermsPersonalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.schoolproject.databinding.ActivityTermsPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}