package com.example.schoolproject.auth.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schoolproject.R;
import com.example.schoolproject.auth.login.LoginActivity;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.MemberApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragSignUp extends Fragment {
    private View view;
    private TextView tv_msg_id, tv_msg_pw, tv_msg_name, tv_msg_email;
    private EditText et_id, et_pw, et_pw2, et_name, et_email;
    private Button btn_signUp;
    private boolean isIdVerified, isPwVerified, isNameVerified, isEmailVerified;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sign_up, container, false);

        // init sharedPerf
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SignupPrefs", Context.MODE_PRIVATE);

        // connect resources
        et_id = view.findViewById(R.id.et_signup_id);
        et_pw = view.findViewById(R.id.et_signup_pw);
        et_pw2 = view.findViewById(R.id.et_signup_pw2);
        et_name = view.findViewById(R.id.et_signup_name);
        et_email = view.findViewById(R.id.et_signup_email);
        btn_signUp = view.findViewById(R.id.btn_signUp);
        tv_msg_id = view.findViewById(R.id.tv_signup_msg_id);
        tv_msg_pw = view.findViewById(R.id.tv_signup_msg_pw);
        tv_msg_name = view.findViewById(R.id.tv_signup_msg_name);
        tv_msg_email = view.findViewById(R.id.tv_signup_msg_email);

        // init msg & flag
        tv_msg_id.setText("");
        tv_msg_pw.setText("");
        tv_msg_name.setText("");
        tv_msg_email.setText("");
        isIdVerified = false;
        isPwVerified = false;
        isNameVerified = false;
        isEmailVerified = false;


        // setting listeners
        // 아이디 입력란의 포커스 변경 리스너 설정
        et_id.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                // 아이디 검사 및 메시지 업데이트
                String inputId = et_id.getText().toString();
                if (inputId.isEmpty()) {
                    // 입력된 아이디가 없는 경우
                    tv_msg_id.setText("아이디를 입력해주세요.");
                    tv_msg_id.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isIdVerified = false;
                } else if (inputId.contains(" ")) {
                    tv_msg_id.setText("공백은 사용할 수 없습니다.");
                    tv_msg_id.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isIdVerified = false;
                } else {
                    // 아이디가 입력된 경우, 검사 로직을 여기에 추가
                    // 예: 아이디 중복 체크 등
                    MemberApiService memberApiService = new MemberApiService();
                    Call<Member> call = memberApiService.getMemberByLoginId(inputId);
                    call.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if (response.isSuccessful()){
                                tv_msg_id.setText("해당 아이디가 이미 존재합니다."); // 결과에 따라 메시지 업데이트
                                tv_msg_id.setTextColor(getResources().getColor(R.color.colorEveryTime));
                                isIdVerified = false;
                            } else {
                                tv_msg_id.setText("사용 가능한 아이디입니다."); // 결과에 따라 메시지 업데이트
                                tv_msg_id.setTextColor(getResources().getColor(R.color.colorAccent));
                                isIdVerified = true;
                            }
                        }
                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            tv_msg_id.setText("인터넷 연결을 확인해주세요."); // 결과에 따라 메시지 업데이트
                            tv_msg_id.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        }
                    });
                }
            }
        });

        // 비밀번호 입력란의 포커스 변경 리스너 설정
        et_pw.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String inputPw = et_pw.getText().toString();
                String inputPw2 = et_pw2.getText().toString();
                if (inputPw.isEmpty() && inputPw2.isEmpty()){
                    tv_msg_pw.setText("비밀번호를 입력해주세요.");
                    tv_msg_pw.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isPwVerified = false;
                }else if (inputPw.contains(" ") || inputPw2.contains(" ")){
                        tv_msg_pw.setText("공백은 사용할 수 없습니다."); // 결과에 따라 메시지 업데이트
                        tv_msg_pw.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        isPwVerified = false;
                } else{
                    if (inputPw.equals(inputPw2)){
                        tv_msg_pw.setText("비밀번호가 일치합니다."); // 결과에 따라 메시지 업데이트
                        tv_msg_pw.setTextColor(getResources().getColor(R.color.colorAccent));
                        isPwVerified = true;
                    } else {
                        tv_msg_pw.setText("비밀번호가 일치하지 않습니다."); // 결과에 따라 메시지 업데이트
                        tv_msg_pw.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        isPwVerified = false;
                    }
                }
            }
        });

        // 비밀번호 확인 입력란의 포커스 변경 리스너 설정
        et_pw2.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String inputPw = et_pw.getText().toString();
                String inputPw2 = et_pw2.getText().toString();
                if (inputPw.isEmpty() && inputPw2.isEmpty()){
                    tv_msg_pw.setText("비밀번호를 입력해주세요.");
                    tv_msg_pw.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isPwVerified = false;
                }else if (inputPw.contains(" ") || inputPw2.contains(" ")){
                    tv_msg_pw.setText("공백은 사용할 수 없습니다."); // 결과에 따라 메시지 업데이트
                    tv_msg_pw.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isPwVerified = false;
                } else{
                    if (inputPw.equals(inputPw2)){
                        tv_msg_pw.setText("비밀번호가 일치합니다."); // 결과에 따라 메시지 업데이트
                        tv_msg_pw.setTextColor(getResources().getColor(R.color.colorAccent));
                        isPwVerified = true;
                    } else {
                        tv_msg_pw.setText("비밀번호가 일치하지 않습니다."); // 결과에 따라 메시지 업데이트
                        tv_msg_pw.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        isPwVerified = false;
                    }
                }
            }
        });


        // 이름 입력란의 포커스 변경 리스너 설정
        et_name.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus){
                String inputName = et_name.getText().toString().trim();
                if (inputName.isEmpty()){
                    tv_msg_name.setText("이름을 입력해주세요."); // 결과에 따라 메시지 업데이트
                    tv_msg_name.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isNameVerified = false;
                } else if (inputName.contains(" ")){
                    tv_msg_name.setText("공백은 사용할 수 없습니다."); // 결과에 따라 메시지 업데이트
                    tv_msg_name.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isNameVerified = false;
                } else {
                    tv_msg_name.setText("멋진 이름이네요!"); // 결과에 따라 메시지 업데이트
                    tv_msg_name.setTextColor(getResources().getColor(R.color.colorAccent));
                    isNameVerified = true;
                }
            }
        });


        // 이메일 입력란의 포커스 변경 리스너 설정
        et_email.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                // 이메일 검사 및 메시지 업데이트
                String inputEmail = et_email.getText().toString();
                // 이메일 검사 로직을 여기에 추가
                // 예: 이메일 형식 검사 등
                if (inputEmail.isEmpty()){
                    tv_msg_email.setText("이메일을 입력해주세요."); // 결과에 따라 메시지 업데이트
                    tv_msg_email.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isEmailVerified = false;
                } else if (inputEmail.contains(" ")){
                    tv_msg_email.setText("공백은 사용할 수 없습니다."); // 결과에 따라 메시지 업데이트
                    tv_msg_email.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isEmailVerified = false;
                } else if (isValidEmail(inputEmail)){
                    // 유 효
                    MemberApiService apiService = new MemberApiService();
                    Call<Member> call = apiService.getMemberByEmail(inputEmail);
                    call.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if (response.isSuccessful()){
                                tv_msg_email.setText("해당 이메일은 이미 사용되었습니다."); // 결과에 따라 메시지 업데이트
                                tv_msg_email.setTextColor(getResources().getColor(R.color.colorEveryTime));
                                isEmailVerified = false;
                            } else {
                                tv_msg_email.setText("사용 가능한 이메일입니다."); // 결과에 따라 메시지 업데이트
                                tv_msg_email.setTextColor(getResources().getColor(R.color.colorAccent));
                                isEmailVerified = true;
                            }
                        }
                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            tv_msg_email.setText("인터넷 연결을 확인해주세요."); // 결과에 따라 메시지 업데이트
                            tv_msg_email.setTextColor(getResources().getColor(R.color.colorEveryTime));
                        }
                    });

                } else {
                    tv_msg_email.setText("유효하지 않은 이메일 형식입니다."); // 결과에 따라 메시지 업데이트
                    tv_msg_email.setTextColor(getResources().getColor(R.color.colorEveryTime));
                    isEmailVerified = false;
                }
            }
        });
        // 마지막 입력 항목인 이메일 입력후 엔터 누르면 포커스 해제
        et_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_email.clearFocus(); // 포커스 해제
                }
                return false; // 다른 키 이벤트 처리를 위해 false 반환
            }
        });


        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 포커스 모두 해제 (검증작업 실행)
                et_id.clearFocus();
                et_pw.clearFocus();
                et_pw2.clearFocus();
                et_name.clearFocus();
                et_email.clearFocus();

                String sid = sharedPreferences.getString("sid", null);
                String univ = sharedPreferences.getString("univ", null);
                String id = et_id.getText().toString().trim();
                String pw = et_pw.getText().toString().trim();
                String pw2 = et_pw2.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String email = et_email.getText().toString().trim();


                // 모든 항목 입력 검사
                if (!isIdVerified || !isPwVerified || !isNameVerified || !isEmailVerified){
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    Toast.makeText(getContext(), "입력하신 내용을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    MemberApiService memberApiService = new MemberApiService();
                    Member newMember = new Member();
                    newMember.setLoginId(id);
                    newMember.setPassword(pw);
                    newMember.setName(name);
                    newMember.setSchoolName(univ);
                    newMember.setStudentId(Integer.parseInt(sid));
                    newMember.setEmail(email);

                    Call<Member> call = memberApiService.createMember(newMember);
                    call.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(view.getContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                // move to LoginActivity
                                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            } else {
                                // hide keyboard
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                                Toast.makeText(view.getContext(), "입력하신 내용을 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            // hide keyboard
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                            Toast.makeText(view.getContext(), "네트워크 오류입니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    // 정규 표현식을 이용한 간단한 이메일 검사
    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }
}
