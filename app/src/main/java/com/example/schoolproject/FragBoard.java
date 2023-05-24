package com.example.schoolproject;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class FragBoard extends Fragment {
    private View view;
    private ViewPager2 viewPager2;
    private TextView tv_board_notice;
    private TextView tv_board_tip;
    private TextView tv_board_report;
    private TextView tv_board_qna;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_board, container,false);
        viewPager2 = view.findViewById(R.id.board_viewpager);

        tv_board_notice = view.findViewById(R.id.tv_board_notice);
        tv_board_tip = view.findViewById(R.id.tv_board_tip);
        tv_board_report = view.findViewById(R.id.tv_board_report);
        tv_board_qna = view.findViewById(R.id.tv_board_qna);

        // setting ViewPager
        BoardViewPagerAdapter adapter = new BoardViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new FragBoardNotice());
        adapter.addFragment(new FragBoardTip());
        adapter.addFragment(new FragBoardReport());
        adapter.addFragment(new FragBoardQnA());

        viewPager2.setSaveEnabled(false);
        //viewPager2.setOffscreenPageLimit(10);
        viewPager2.setAdapter(adapter);

        // set OnPageChangedListener
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // when page selected
                switch (position){
                    case 0:
                        setTextViewStyle(tv_board_notice);
                        setTextViewDefaultStyle(tv_board_tip);
                        setTextViewDefaultStyle(tv_board_report);
                        setTextViewDefaultStyle(tv_board_qna);
                        break;
                    case 1:
                        setTextViewStyle(tv_board_tip);
                        setTextViewDefaultStyle(tv_board_notice);
                        setTextViewDefaultStyle(tv_board_report);
                        setTextViewDefaultStyle(tv_board_qna);
                        break;
                    case 2:
                        setTextViewStyle(tv_board_report);
                        setTextViewDefaultStyle(tv_board_tip);
                        setTextViewDefaultStyle(tv_board_notice);
                        setTextViewDefaultStyle(tv_board_qna);
                        break;
                    case 3:
                        setTextViewStyle(tv_board_qna);
                        setTextViewDefaultStyle(tv_board_tip);
                        setTextViewDefaultStyle(tv_board_report);
                        setTextViewDefaultStyle(tv_board_notice);
                        break;
                }
            }
        });
        tv_board_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(0);
            }
        });
        tv_board_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(1);
            }
        });
        tv_board_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(2);
            }
        });
        tv_board_qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(3);
            }
        });


        return view;
    }

    private void setTextViewStyle(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
    }
    private void setTextViewDefaultStyle(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        textView.setTypeface(null, Typeface.NORMAL);
        textView.setTextColor(Color.parseColor("#B8B8B8"));
    }

    @Override
    public void onDestroyView() {
        viewPager2.setAdapter(null);
        super.onDestroyView();
    }
}
