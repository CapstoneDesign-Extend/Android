package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schoolproject.restapi.Member;
import com.example.schoolproject.restapi.MemberApiService;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private DataBaseHelper dbHelper;
    private EditText et_id, et_pw;
    private Button btn_signUp;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // make dbHelper instance
        dbHelper = new DataBaseHelper(this);

        // connect resources
        et_id = findViewById(R.id.et_signUp_id);
        et_pw = findViewById(R.id.et_signUp_pw);
        btn_signUp = findViewById(R.id.btn_signUp);

        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_signUp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // setting listeners
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();

                if (id.isEmpty() || pw.isEmpty()){
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    Snackbar.make(v, "아이디와 비밀번호를 입력해주세요.", 500).show();
                }else{
                    MemberApiService memberApiService = new MemberApiService();
                    Member newMember = new Member();
                    newMember.setLoginId(id);
                    newMember.setPassword(pw);
                    Call<Member> call = memberApiService.createMember(newMember);
                    call.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                // move to LoginActivity
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                                } else {
                                // hide keyboard
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                                Snackbar.make(v, "[Error] Can't get Response", 500).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            // hide keyboard
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                            Snackbar.make(v, "[Error] onFailure called", 500).show();
                        }
                    });

//                    // SQLite DB logic
////                    ContentValues values = new ContentValues();
////                    values.put("id", id);
////                    values.put("pw", pw);
////                    // insert data
////                    long result = dbHelper.insertData("User", values);
////                    if (result == -1){
////                        // hide keyboard
////                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
////                        Snackbar.make(v, "해당 아이디가 이미 존재합니다.", 500).show();
//                    } else {
//                        Toast.makeText(SignUpActivity.this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}