package com.example.schoolproject.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.RoomSQLiteQuery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.schoolproject.model.retrofit.MemberApiService;
import com.example.schoolproject.mypage.activity.BlockedHistoryActivity;
import com.example.schoolproject.mypage.activity.EditPasswordActivity;
import com.example.schoolproject.mypage.activity.RulesActivity;
import com.example.schoolproject.mypage.activity.TermsPersonalActivity;
import com.example.schoolproject.mypage.activity.TermsServiceActivity;
import com.example.schoolproject.mypage.activity.TermsYouthActivity;
import com.example.schoolproject.test.TestRetrofit;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // =======================  비밀번호 변경 동작  ===========================
        binding.tvMypageEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, EditPasswordActivity.class);
                startActivity(intent);
            }
        });
        // =======================  이용 제한 내역 열기  ===========================
        binding.tvMypageHistoryBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, BlockedHistoryActivity.class);
                startActivity(intent);
            }
        });
        // =======================  커뮤니티 이용규칙 열기  ===========================
        binding.tvMypageRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });
        // =======================  서비스 이용약관 열기  ===========================
        binding.tvMypageTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, TermsServiceActivity.class);
                startActivity(intent);
            }
        });
        // =======================  개인정보 처리방침 열기  ===========================
        binding.tvMypagePolicyPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, TermsPersonalActivity.class);
                startActivity(intent);
            }
        });
        // =======================  청소년 보호정책 열기  ===========================
        binding.tvMypagePolicyYouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, TermsYouthActivity.class);
                startActivity(intent);
            }
        });
        // =======================  회원 탈퇴  ===========================
        binding.tvMypageWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MyPageActivity.this, R.style.RoundedDialog)
                        .setTitle("회원 탈퇴")
                        .setMessage("회원 탈퇴하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MemberApiService apiService = new MemberApiService();
                                Call<Void> call = apiService.deleteMember(sPrefs.getLong("id", -1 ));
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                            // 로그아웃 동작
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
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "서버로부터 응답을 받을 수 없습니댜.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                // 긍정적인 버튼 (예)의 텍스트 색상 변경
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));

                // 부정적인 버튼 (아니오)의 텍스트 색상 변경
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
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