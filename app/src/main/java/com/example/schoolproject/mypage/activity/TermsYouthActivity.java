package com.example.schoolproject.mypage.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolproject.databinding.ActivityTermsServiceBinding;
import com.example.schoolproject.databinding.ActivityTermsYouthBinding;

public class TermsYouthActivity extends AppCompatActivity {
    ActivityTermsYouthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsYouthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}