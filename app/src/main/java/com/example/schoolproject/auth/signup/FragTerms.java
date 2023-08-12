package com.example.schoolproject.auth.signup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragTermsBinding;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.HashMap;
import java.util.Map;


public class FragTerms extends Fragment {
    private View view;
    private FragTermsBinding binding;
    Map<Integer, Integer> viewOriginalHeights = new HashMap<>();  // animation 동작시 기존 뷰의 원래 높이를 각각 저장할 datatype


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragTermsBinding.inflate(inflater, container, false);

       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // prevent memory leaks
    }

    private void setupListeners(){
        // connect checkbox + textview
        binding.tvTermsChkAll.setOnClickListener(v -> binding.cbTermsChkAll.setChecked(!binding.cbTermsChkAll.isChecked()));
        binding.tvTitleTermsService.setOnClickListener(v -> binding.cbTermsService.setChecked(!binding.cbTermsService.isChecked()));
        binding.tvTermsTitlePersonal.setOnClickListener(v -> binding.cbTermsPersonal.setChecked(!binding.cbTermsPersonal.isChecked()));
        binding.tvTermsTitleCommunity.setOnClickListener(v -> binding.cbTermsCommunity.setChecked(!binding.cbTermsCommunity.isChecked()));
        binding.tvTermsTitleAdvertisement.setOnClickListener(v -> binding.cbTermsAdvertisement.setChecked(!binding.cbTermsAdvertisement.isChecked()));
        binding.tvTermsTitleUname.setOnClickListener(v -> binding.cbTermsUname.setChecked(!binding.cbTermsUname.isChecked()));
        binding.tvTermsTitleAge.setOnClickListener(v -> binding.cbTermsAge.setChecked(!binding.cbTermsAge.isChecked()));

        // ChkAll setting
        binding.cbTermsChkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.cbTermsService.setChecked(isChecked);
            binding.cbTermsPersonal.setChecked(isChecked);
            binding.cbTermsCommunity.setChecked(isChecked);
            binding.cbTermsAdvertisement.setChecked(isChecked);
        });

        // hide content with animation
        binding.cbTermsService.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            handleCheckBoxChange(binding.cbTermsService, binding.tvTermsContentService);
        }));
        binding.cbTermsPersonal.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            handleCheckBoxChange(binding.cbTermsPersonal, binding.tvTermsContentPersonal);
        }));
        binding.cbTermsCommunity.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            handleCheckBoxChange(binding.cbTermsCommunity, binding.tvTermsContentCommunity);
        }));
        binding.cbTermsAdvertisement.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            handleCheckBoxChange(binding.cbTermsAdvertisement, binding.tvTermsContentAdvertisement);
        }));
        binding.cbTermsUname.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            handleCheckBoxChange(binding.cbTermsUname, binding.tvTermsContentUname);
        }));
        binding.cbTermsAge.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            handleCheckBoxChange(binding.cbTermsAge, binding.tvTermsContentAge);
        }));

        // set inner scrollable
        TextView[] scrollableTextViews = {
                binding.tvTermsContentService,
            binding.tvTermsContentPersonal,
            binding.tvTermsContentCommunity,
            binding.tvTermsContentAdvertisement,
            binding.tvTermsContentUname,
            binding.tvTermsContentAge
        };
        for (TextView textView : scrollableTextViews){
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    TextView tv = (TextView) v;
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        if (!tv.canScrollVertically(-1) && !tv.canScrollVertically(1)){
                            // TextView가 스크롤이 안되는 상태일 떄
                            binding.scrollviewTerms.requestDisallowInterceptTouchEvent(false);
                        } else {
                            // TextView가 스크롤 가능한 상태일 떄
                            binding.scrollviewTerms.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    return false;
                }
            });
        }

        binding.tvAuthPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.cbTermsService.isChecked() && binding.cbTermsPersonal.isChecked() && binding.cbTermsCommunity.isChecked() && binding.cbTermsUname.isChecked() && binding.cbTermsAge.isChecked()){
                    // move to next fragment
                    FragSignUp fragSignUp = new FragSignUp();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // add animation
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                    transaction.replace(R.id.frameWrapper_signup, fragSignUp);
                    transaction.addToBackStack(null);  // if back pressed, move previous fragment
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "모든 약관에 동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    // toggle animation
    private void handleCheckBoxChange(MaterialCheckBox checkBox, View targetView) {
        Integer originalHeight = viewOriginalHeights.get(targetView.getId());
        if (originalHeight == null) {originalHeight = 0;}

        // 원래 높이가 저장되어 있지 않다면 측정 및 저장
        if (originalHeight == 0){
            originalHeight = targetView.getHeight();
            viewOriginalHeights.put(targetView.getId(), originalHeight);
        }
        if (checkBox.isChecked()) {
            collapseView(targetView);
        } else {
            expandView(targetView, originalHeight);
        }
    }

    // animations
    public void collapseView(View view) {
        int origHeight = view.getHeight();
        view.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(origHeight, 0);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = animatedValue;
            view.setLayoutParams(layoutParams);
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    public void expandView(View view, int targetHeight) {
        // 1. 뷰의 초기 높이를 0으로 설정
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = 0;
//        view.setLayoutParams(layoutParams);
//        view.requestLayout(); // layout 을 다시 계산

        // 2. Visibility 를 VISIBLE 로 설정
        view.setVisibility(View.VISIBLE);

        // 추가:: 현재 높이를 가져오기
        int currentHeight = view.getHeight();  // 이거 추가해서 해결됨
        // 문제: 체크 해제될 때, content가 전체 크기로 잠깐 나타났다가, 다시 사라지고 커지는 애니메이션 시작. 부자연스러움.
        // 기존 코드 : 메서드가 호출될 때 높이가 0으로 설정된 채로 호출 + ValueAnimator의 시작 값도 0
        // 개선 코드 : 현재 높이를 ValueAnimator의 시작값으로 줘서 해결

        // 3. Animation 실행
        ValueAnimator valueAnimator = ValueAnimator.ofInt(currentHeight, targetHeight);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = animatedValue;
            view.setLayoutParams(layoutParams);
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

}
