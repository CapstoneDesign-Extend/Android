package com.example.schoolproject.auth.signup;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.schoolproject.R;
import com.example.schoolproject.auth.login.LoginActivity;
import com.example.schoolproject.model.retrofit.Member;
import com.example.schoolproject.model.retrofit.MemberApiService;
import com.example.schoolproject.test.DataBaseHelper;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragSignUp extends Fragment {
    private View view;
    private EditText et_id, et_pw, et_pw2, et_name, et_email;
    private Button btn_signUp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sign_up, container, false);

        // init sharedPerf
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SignupPrefs", Context.MODE_PRIVATE);

        // connect resources
        et_id = view.findViewById(R.id.et_signUp_id);
        et_pw = view.findViewById(R.id.et_signUp_pw);
        et_pw2 = view.findViewById(R.id.et_signUp_pw2);
        et_name = view.findViewById(R.id.et_signUp_name);
        et_email = view.findViewById(R.id.et_signUp_email);
        btn_signUp = view.findViewById(R.id.btn_signUp);



        // setting listeners
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sid = sharedPreferences.getString("sid", null);
                String univ = sharedPreferences.getString("univ", null);
                String id = et_id.getText().toString();
                String pw = et_pw.getText().toString();
                String pw2 = et_pw2.getText().toString();
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();


                if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || email.isEmpty()){
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    Toast.makeText(getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if (pw != pw2){
                        Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        MemberApiService memberApiService = new MemberApiService();
                        Member newMember = new Member();
                        newMember.setLoginId(id);
                        newMember.setPassword(pw);
                        newMember.setName(name);
                        newMember.setSchoolName(univ);
                        newMember.setStudentId(Integer.parseInt(sid));

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
                                    Snackbar.make(v, "[Error] Can't get Response", 500).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Member> call, Throwable t) {
                                // hide keyboard
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                                Snackbar.make(v, "[Error] onFailure called", 500).show();
                            }
                        });
                    }
                }
            }
        });

        return view;
    }
}
