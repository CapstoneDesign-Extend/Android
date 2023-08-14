package com.example.schoolproject.auth.signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.schoolproject.R;
import com.example.schoolproject.auth.login.LoginActivity;
import com.example.schoolproject.model.retrofit.CnetApiService;
import com.example.schoolproject.model.retrofit.CnetSchool;
import com.example.schoolproject.model.retrofit.CnetSchoolResponse;
import com.example.schoolproject.model.retrofit.Member;
import com.example.schoolproject.model.retrofit.MemberApiService;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragSelUniv extends Fragment {
    private View view;
    private TextView tv_next;
    private EditText et_sid, et_univ;
    private ListView lv_schoolNameContainer;
    private SchoolListAdapter adapter;

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleEditorAction();
            }
            return false; // 다른 키 이벤트 처리를 위해 false 반환
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sel_univ, container, false);

        et_sid = view.findViewById(R.id.et_signUp_sid);
        et_univ = view.findViewById(R.id.et_signUp_univ);
        tv_next = view.findViewById(R.id.tv_auth_next);
        lv_schoolNameContainer = view.findViewById(R.id.listview_signup_school_name);

        adapter = new SchoolListAdapter(getContext());
        lv_schoolNameContainer.setAdapter(adapter);

        // 학교 데이터 제공 api 통신
        CnetApiService apiService = new CnetApiService();
        Call<CnetSchoolResponse> call = apiService.getSchoolList("api", "SCHOOL", "json", "univ_list", "1", "444");
        call.enqueue(new Callback<CnetSchoolResponse>() {
            @Override
            public void onResponse(Call<CnetSchoolResponse> call, Response<CnetSchoolResponse> response) {
                if (response.isSuccessful()){
                    CnetSchoolResponse schoolResponse = response.body();
                    if (schoolResponse != null){
                        List<CnetSchool> schools = schoolResponse.getDataSearch().getContent();
                        List<String> schoolNames = new ArrayList<>();
                        for (CnetSchool school : schools){
                            schoolNames.add(school.getSchoolName());
                        }
                        adapter.setSchoolList(schoolNames);
                    }else {
                        Toast.makeText(getContext(), "학교 이름을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // api response failed
                    Toast.makeText(getContext(), "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CnetSchoolResponse> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류입니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        // 실시간 학교 검색 리스너
        et_univ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lv_schoolNameContainer.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_next.setVisibility(View.INVISIBLE);



            }
        });

        // 리스트의 학교를 클릭하면 자동 입력
        lv_schoolNameContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSchoolName = (String) parent.getItemAtPosition(position);
                et_univ.setText(selectedSchoolName);
                lv_schoolNameContainer.setVisibility(View.GONE);
                tv_next.setVisibility(View.VISIBLE);
            }
        });

        // 검색 결과가 하나만 남았을때, 엔터 누르면 자동 입력
        et_univ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    handleEditorAction();
                }
                return false; // 다른 키 이벤트 처리를 위해 false 반환
            }
        });

        // 다음 버튼
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_sid.getText().toString().trim().isEmpty() || et_univ.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "학번과 학교를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    int inputSid = Integer.parseInt(et_sid.getText().toString().trim());
                    String inputUniv = et_univ.getText().toString().trim();

                    MemberApiService memberApiService = new MemberApiService();
                    Call<List<Member>> call = memberApiService.getMemberByStudentId(inputSid);
                    call.enqueue(new Callback<List<Member>>() {
                        @Override
                        public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                            if (response.isSuccessful()){
                                List<Member> members = response.body();
                                if (members != null && !members.isEmpty()){
                                    // 해당 학번을 가진 회원이 존재하는 경우
                                    boolean isDuplicate = false;
                                    for (Member member : members){
                                        if (member.getStudentId() == inputSid && member.getSchoolName().equals(inputUniv)){
                                            // 입력한 학교의 학번이 이미 사용된 경우
                                            isDuplicate = true;
                                            break;
                                        }
                                    }
                                    if (isDuplicate){
                                        Toast.makeText(getContext(), "입력하신 학교의 학번은 이미 사용되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        // 해당 학번은 존재하지만 학교가 다른 경우: 가입 가능
                                        // save data in sharedPref
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SignupPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("sid", String.valueOf(inputSid));
                                        editor.putString("univ", inputUniv);
                                        editor.commit();

                                        // move to next fragment
                                        FragTerms fragTerms = new FragTerms();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                        // add animation
                                        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                                        transaction.replace(R.id.frameWrapper_signup, fragTerms);
                                        transaction.addToBackStack(null);  // if back pressed, move previous fragment
                                        transaction.commit();
                                    }

                                } else {
                                    // 원래 서버가 빈 응답을 보내주면 여기서 가입 처리를 해줘야 하는데,
                                    // 서버가 빈응답이 아니라 404를 보내서 밑에서 처리함.
                                }
                            } else {
                                // 해당 학번 자체가 존재하지 않는 경우(404 응답시): 가입 가능
                                // save data in sharedPref
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SignupPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("sid", String.valueOf(inputSid));
                                editor.putString("univ", inputUniv);
                                editor.commit();

                                // move to next fragment
                                FragTerms fragTerms = new FragTerms();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                // add animation
                                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                                transaction.replace(R.id.frameWrapper_signup, fragTerms);
                                transaction.addToBackStack(null);  // if back pressed, move previous fragment
                                transaction.commit();
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Member>> call, Throwable t) {
                            Toast.makeText(getContext(), "네트워크 오류입니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        return view;
    }
    // 엔터를 누르면 학교 이름 자동 입력
    private void handleEditorAction(){
        String enteredText = et_univ.getText().toString().trim();
        if (!enteredText.isEmpty() && adapter.getCount() == 1) {
            String schoolNameInList = adapter.getItem(0).toString();

            et_univ.setText(schoolNameInList);
            lv_schoolNameContainer.setVisibility(View.GONE);
            tv_next.setVisibility(View.VISIBLE);
            //hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_univ.getWindowToken(), 0);

            tv_next.performClick();
        }
    }

}
