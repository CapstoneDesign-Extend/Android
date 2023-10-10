package com.example.schoolproject.mypage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ActivityEditPasswordBinding;

public class EditPasswordActivity extends AppCompatActivity {
    ActivityEditPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}