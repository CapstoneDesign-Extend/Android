package com.example.schoolproject.mypage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ActivityEditPasswordBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.MemberApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {
    ActivityEditPasswordBinding binding;
    private boolean isPwVerified;
    private SharedPreferences sPrefs;
    private Long curUserId;
    private String curUserPw;
    private Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 초기화
        isPwVerified = false;
        sPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        curUserId = sPrefs.getLong("id", -1);
        curUserPw = sPrefs.getString("pw", null);
        Gson gson = new Gson();
        member = new Member();
        String json = sPrefs.getString("memberJson", null);
        if (json != null){
            member = gson.fromJson(json, Member.class);
        }

        // ==============  비번
        binding.etSignupPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String inputPw = binding.etSignupPw.getText().toString();
                    String inputPw2 = binding.etSignupPw3.getText().toString();
                    if (inputPw.isEmpty() && inputPw2.isEmpty()){
                        binding.tvSignupMsgPw2.setText("비밀번호를 입력해주세요.");
                        binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        isPwVerified = false;
                    }else if (inputPw.contains(" ") || inputPw2.contains(" ")){
                        binding.tvSignupMsgPw2.setText("공백은 사용할 수 없습니다."); // 결과에 따라 메시지 업데이트
                        binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        isPwVerified = false;
                    } else{
                        if (inputPw.equals(inputPw2)){
                            binding.tvSignupMsgPw2.setText("비밀번호가 일치합니다."); // 결과에 따라 메시지 업데이트
                            binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorAccent));
                            isPwVerified = true;
                        } else {
                            binding.tvSignupMsgPw2.setText("비밀번호가 일치하지 않습니다."); // 결과에 따라 메시지 업데이트
                            binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorEveryTime));
                            isPwVerified = false;
                        }
                    }
                }
            }
        });
        // ==============  비번2
        binding.etSignupPw3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String inputPw = binding.etSignupPw.getText().toString();
                String inputPw2 = binding.etSignupPw3.getText().toString();
                if (inputPw.isEmpty() && inputPw2.isEmpty()){
                    binding.tvSignupMsgPw2.setText("비밀번호를 입력해주세요.");
                    binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isPwVerified = false;
                }else if (inputPw.contains(" ") || inputPw2.contains(" ")){
                    binding.tvSignupMsgPw2.setText("공백은 사용할 수 없습니다."); // 결과에 따라 메시지 업데이트
                    binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isPwVerified = false;
                } else{
                    if (inputPw.equals(inputPw2)){
                        binding.tvSignupMsgPw2.setText("비밀번호가 일치합니다."); // 결과에 따라 메시지 업데이트
                        binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorAccent));
                        isPwVerified = true;
                    } else {
                        binding.tvSignupMsgPw2.setText("비밀번호가 일치하지 않습니다."); // 결과에 따라 메시지 업데이트
                        binding.tvSignupMsgPw2.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        isPwVerified = false;
                    }
                }
            }
        });
        // ==============  현재비번
        binding.etSignupPw4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        // ==============  닫기 버튼 동작
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 모든 항목 입력 검사
                if (!isPwVerified){
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    Toast.makeText(getApplicationContext(), "입력하신 내용을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if (binding.etSignupPw4.getText().toString().equals(curUserPw)){
                        // 업데이트 로직
                        member.setPassword(binding.etSignupPw3.getText().toString());
                        MemberApiService apiService = new MemberApiService();
                        Call<Member> call = apiService.updateMember(curUserId, member);
                        call.enqueue(new Callback<Member>() {
                            @Override
                            public void onResponse(Call<Member> call, Response<Member> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "비밀번호를 변경했습니다.", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sPrefs.edit();
                                    editor.putString("pw", member.getPassword());
                                    editor.apply();
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(), "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Member> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }



                }


            }
        });

    }
}