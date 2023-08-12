package com.example.schoolproject.auth.signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.schoolproject.R;

public class FragSelUniv extends Fragment {
    private View view;
    private TextView tv_next;
    private EditText et_sid, et_univ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_sel_univ, container, false);


        et_sid = view.findViewById(R.id.et_signUp_sid);
        et_univ = view.findViewById(R.id.et_signUp_univ);
        tv_next = view.findViewById(R.id.tv_auth_next);

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_sid.getText().toString().trim().isEmpty() || et_univ.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "학번과 학교를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // save data in sharedPref
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SignupPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("sid", et_sid.getText().toString());
                    editor.putString("univ", et_univ.getText().toString());
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
        });

        return view;
    }
}
