package com.example.schoolproject.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.RoomSQLiteQuery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.CameraActivity;
import com.example.schoolproject.R;
import com.example.schoolproject.auth.login.LoginActivity;
import com.example.schoolproject.databinding.ActivityMyPageBinding;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.test.TestRetrofit;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MyPageActivity extends AppCompatActivity {
    private ActivityMyPageBinding binding;
    private SharedPreferences sPrefs;
    private Member member;

    @Override
    protected void onResume() {
        super.onResume();
        //   ******* 초기화 동작 정의  ************
        // 사용자 id, 이름, 학번, 학교 가져오기
        Gson gson = new Gson();
        sPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String memberJson = sPrefs.getString("memberJson", null);
        member = gson.fromJson(memberJson, Member.class);
        // VIEW에 MEMBER 정보 표시
        binding.mypageUserId.setText(member.getLoginId());
        binding.mypageUserName.setText(member.getName());
        binding.mypageUserSid.setText(String.valueOf(member.getStudentId()));
        binding.mypageSchoolName.setText(member.getSchoolName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 바인딩 초기화
        binding = ActivityMyPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_myPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // =======================  학생 인증 동작  ===========================
        binding.tvMypageAuthSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (member.getDepartment() != null && !member.getDepartment().isEmpty()){
                    Toast.makeText(getApplicationContext(), "이미 학생증 인증을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        // 카메라 액티비티 호출
                        Intent intent = new Intent(MyPageActivity.this, CameraActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "카메라 권한이 거부되었습니다.\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("카메라를 사용하기 위해 권한이 필요합니다.\n\n[설정] > [권한] 에서 권한을 허용해주세요.")
                        .setPermissions(Manifest.permission.CAMERA)
                        .check();


            }
        });

        // =======================  로그아웃 동작  ===========================

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
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