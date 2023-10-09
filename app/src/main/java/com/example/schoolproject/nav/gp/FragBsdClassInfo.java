package com.example.schoolproject.nav.gp;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragBsdClassInfoBinding;
import com.example.schoolproject.model.retrofit.CommentApiService;
import com.example.schoolproject.model.retrofit.CommentCallback;
import com.example.schoolproject.post.PostRecyclerViewAdapter;
import com.example.schoolproject.timetableview.ConvertFormat;
import com.github.tlaabs.timetableview.Schedule;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;

public class FragBsdClassInfo extends BottomSheetDialogFragment {
    private FragBsdClassInfoBinding binding;
    private int idx;
    private ArrayList<Schedule> schedules;
    private ScheduleCallback callback;

    public void setCallback(ScheduleCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragBsdClassInfoBinding.inflate(inflater, container, false);
        Bundle args = getArguments();

        // ======================  정보 조회 ==========================
        if (args != null) {
            idx = args.getInt("idx");
            schedules = (ArrayList<Schedule>) args.getSerializable("schedules");

            binding.tvClassName.setText(schedules.get(0).getClassTitle());
            binding.tvProfessorName.setText(schedules.get(0).getProfessorName());
            binding.tvDay.setText(ConvertFormat.convertDay(schedules.get(0).getDay()));
            binding.tvStartTime.setText(ConvertFormat.convertTime(schedules.get(0).getStartTime()));
            binding.tvEndTime.setText(ConvertFormat.convertTime(schedules.get(0).getEndTime()));
            binding.tvClassroom.setText(schedules.get(0).getClassPlace());
        } else {
            // 못받았을때 예외 메시지
        }



        // ======================  버튼 동작 ==========================
        //  ***** 수정 버튼 *****
        binding.tvEditClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 강의를 추가하는 대화상자(BSD프래그먼트) 호출
                FragBsdClassAdd bottomSheet = new FragBsdClassAdd();
                Bundle args = new Bundle();
                args.putBoolean("isEdit", true);
                args.putInt("idx", idx);
                args.putSerializable("schedules", schedules);

                bottomSheet.setArguments(args);

                if (callback != null) {bottomSheet.setCallback(callback);}
                bottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), bottomSheet.getTag());
                // 작업이 끝나면 bottomSheetDialogFragment 닫기
                dismiss();
            }
        });

        // ***** 삭제 버튼 *****
        binding.tvDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){

                    AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog)
                            .setTitle("강의 삭제")
                            .setMessage("선택한 강의를 삭제할까요?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 강의 삭제
                                    callback.onScheduleDeleted(idx);
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .show();
                    // 긍정적인 버튼 (예)의 텍스트 색상 변경
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorAccent));

                    // 부정적인 버튼 (아니오)의 텍스트 색상 변경
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getActivity().getResources().getColor(R.color.colorAccent));

                }
                // 작업이 끝나면 bottomSheetDialogFragment 닫기
                dismiss();
            }
        });
        // ====================  버튼 동작 끝  =====================

        return binding.getRoot();
    }
}
