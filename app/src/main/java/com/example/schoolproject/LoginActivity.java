package com.example.schoolproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private DataBaseHelper dbHelper;
    private EditText et_id, et_pw;
    private Button btn_login;
    private TextView tv_findPw, tv_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // make dbHelper instance
        dbHelper = new DataBaseHelper(this);

        // connect resources
        et_id = findViewById(R.id.et_login_id);
        et_pw = findViewById(R.id.et_login_pw);
        btn_login = findViewById(R.id.btn_login);
        tv_findPw = findViewById(R.id.tv_login_findPw);
        tv_signUp = findViewById(R.id.tv_login_signUp);

        // setting listeners
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();

                if (id.isEmpty() || pw.isEmpty()){
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    Snackbar.make(v, "아이디와 비밀번호를 입력해주세요.", 200).show();
                }else{

                    // logic: select data from db
                    // 데이터베이스 조회 로직 작성
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    String[] columns = {"id", "pw"};
                    String selection = "id = ? AND pw = ?";
                    String[] selectionArgs = {id, pw};
                    Cursor cursor = db.query("User", columns, selection, selectionArgs, null, null, null);

                    // 데이터 조회 결과 처리
                    if (cursor != null && cursor.moveToFirst()) {
                        @SuppressLint("Range") String dbId = cursor.getString(cursor.getColumnIndex("id"));
                        @SuppressLint("Range") String dbPw = cursor.getString(cursor.getColumnIndex("pw"));

                        String message = "ID: " + dbId + ", PW: " + dbPw;
                        Snackbar.make(v, message, 200).show();
                        Toast.makeText(LoginActivity.this,"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // hide keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                        Snackbar.make(v, "ID가 존재하지 않거나 ID와 PW가 일치하지 않습니다.", 150).show();
                    }

                    // 리소스 해제
                    if (cursor != null) {
                        cursor.close();
                    }
                    db.close();

                }
            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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