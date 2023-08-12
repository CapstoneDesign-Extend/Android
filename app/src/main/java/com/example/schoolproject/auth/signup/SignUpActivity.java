package com.example.schoolproject.auth.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.schoolproject.R;


public class SignUpActivity extends AppCompatActivity {
    private ImageView iv_back, iv_close;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iv_back = findViewById(R.id.iv_back);
        iv_close = findViewById(R.id.iv_close);
        frameLayout = findViewById(R.id.frameWrapper_signup);

        // init==invisible
        toggleBackBtnVisibility(false);

        if (savedInstanceState == null){
            // 첫 생성시, 초기 프래그먼트 설정 및 iv_back 숨기기
            FragmentManager manager = getSupportFragmentManager();
            FragSelUniv fragSelUniv = new FragSelUniv();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.frameWrapper_signup, fragSelUniv);
            transaction.commit();

        } else {
            // 상태 복원시, 백스택의 상태에 따라 iv_back의 가시성 조정
            handleBackButtonVisibility();
        }

        // setting listeners
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
            }
        });
        // 백스택 리스너 추가
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                handleBackButtonVisibility();
            }
        });





    }

    public void toggleBackBtnVisibility(boolean show){
        if (show){
            iv_back.setVisibility(View.VISIBLE);
        } else {
            iv_back.setVisibility(View.INVISIBLE);
        }
    }

    public void handleBackButtonVisibility(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            toggleBackBtnVisibility(true);
        } else {
            toggleBackBtnVisibility(false);
        }
    }

}