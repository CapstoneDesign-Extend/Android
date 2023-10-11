package com.example.schoolproject.auth.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.auth.signup.SignUpActivity;
import com.example.schoolproject.R;
import com.example.schoolproject.MainActivity;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.MemberApiService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private EditText et_id, et_pw;
    private Button btn_login;
    private TextView tv_findPw, tv_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // init SharedPreferences
        sharedPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        // check login state
        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn",false);
        if (isLoggedIn){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        // connect resources
        et_id = findViewById(R.id.et_login_id);
        et_pw = findViewById(R.id.et_login_pw);
        btn_login = findViewById(R.id.btn_login);
        tv_findPw = findViewById(R.id.tv_login_findPw);
        tv_signUp = findViewById(R.id.tv_login_signUp);

        // setting listeners
        et_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    et_id.selectAll();
                }
            }
        });

        et_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    et_pw.selectAll();
                }
            }
        });
        et_pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // detect enter key
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    btn_login.performClick();
                    return true;
                }
                return false;
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 로그인 버튼 클릭시 동작
                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();

                if (id.isEmpty() || pw.isEmpty()){
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    Snackbar.make(v, "아이디와 비밀번호를 입력해주세요.", 500).show();
                }else{
                    MemberApiService memberApiService = new MemberApiService();

                    Call<Member> call = memberApiService.getMemberByLoginId(id);
                    call.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if (response.isSuccessful()){
                                Member member = response.body();
                                if (member != null && member.getPassword().equals(pw)){
                                    // 회원 정보가 일치하면 로그인 성공 처리
                                    String message = "ID: " + member.getLoginId() + ", PW: " +member.getPassword();
                                    Snackbar.make(v, message, 200).show();
                                    Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();

                                    // save login state + userID + Member
                                    Gson gson = new Gson();
                                    String memberJson = gson.toJson(member);
                                    editor.putString("memberJson", memberJson);
                                    editor.putLong("id", member.getId());
                                    editor.putString("pw", member.getPassword());
                                    editor.putString("loginId", id);
                                    editor.putBoolean("isLoggedIn", true);
                                    if (member.getDepartment() != null && !member.getDepartment().isEmpty()){
                                        editor.putBoolean("isCertified", true);
                                    }else{
                                        editor.putBoolean("isCertified", false);
                                    }
                                    editor.apply();

                                    // move to MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //hide keyboard
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                                    Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // hide keyboard
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                                Toast.makeText(LoginActivity.this, "해당 계정이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            // hide keyboard
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                            Toast.makeText(LoginActivity.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                et_id.setText("");
                et_pw.setText("");
            }
        });
        tv_findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "다시 한번 잘 생각해 보세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }


}