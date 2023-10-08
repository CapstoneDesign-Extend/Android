package com.example.schoolproject.nav.gp;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragBsdClassAddBinding;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FragBottomSheetDialog extends BottomSheetDialogFragment {
    private ScheduleCallback callback;
    private Time startTime, endTime;  // 시간표에서 사용하는 Time 클래스
    private int day;  // 월, 화, 수, 목, 금 = 0, 1, 2, 3, 4
    boolean startTimeIsNULL = true;
    boolean endTimeIsNULL = true;
    private FragBsdClassAddBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragBsdClassAddBinding.inflate(inflater, container, false);

        // *****************   수업시간 설정   *****************
        binding.etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerPopupDialogTwoButton octDialog = new TimePickerPopupDialogTwoButton(getContext(), new TimePickerPopupDialogClickListener() {
                    @Override
                    public void onPositiveClick(int h, int m) {
                        //Log.d(TAG, "onPositiveClick: "+ h + "시" + m + "분");
                        String timeString = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                        binding.etStartTime.setText(timeString);
                        binding.etStartTime.setTextColor(getResources().getColor(R.color.black));
                        startTime = new Time(h, m);
                        startTimeIsNULL = false;
                    }
                    @Override
                    public void onNegativeClick() {
                        //Log.d(TAG, "NO click: ");
                    }
                });

                Calendar calendar = Calendar.getInstance();
                octDialog.setSetHourValue(calendar.get(Calendar.HOUR_OF_DAY));
                octDialog.setSetMinuteVale(calendar.get(Calendar.MINUTE));
                octDialog.setCanceledOnTouchOutside(false);  // 외부 터치 시 꺼짐.
                octDialog.setCancelable(true);  //  back 버튼으로 취소 가능

                octDialog.show();
            }
        });
        binding.etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerPopupDialogTwoButton octDialog = new TimePickerPopupDialogTwoButton(getContext(), new TimePickerPopupDialogClickListener() {
                    @Override
                    public void onPositiveClick(int h, int m) {
                        //Log.d(TAG, "onPositiveClick: "+ h + "시" + m + "분");
                        String timeString = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                        binding.etEndTime.setText(timeString);
                        binding.etEndTime.setTextColor(getResources().getColor(R.color.black));
                        endTime = new Time(h, m);
                        endTimeIsNULL = false;
                    }
                    @Override
                    public void onNegativeClick() {
                        //Log.d(TAG, "NO click: ");
                    }
                });

                Calendar calendar = Calendar.getInstance();
                octDialog.setSetHourValue(calendar.get(Calendar.HOUR_OF_DAY));
                octDialog.setSetMinuteVale(calendar.get(Calendar.MINUTE));
                octDialog.setCanceledOnTouchOutside(false);  // 외부 터치 시 꺼짐.
                octDialog.setCancelable(true);  //  back 버튼으로 취소 가능

                octDialog.show();
            }
        });
        binding.spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                switch (selectedValue){
                    case "월요일": day = 0; break;
                    case "화요일": day = 1; break;
                    case "수요일": day = 2; break;
                    case "목요일": day = 3; break;
                    case "금요일": day = 4; break;
                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // ***********************  수업시간 설정 끝  ***************************



        // 저장 버튼을 눌렀을때 동작
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classTitle = binding.etClassName.getText().toString();
                String classPlace = binding.etClassroom.getText().toString();
                String professorName = binding.etProfessorName.getText().toString();

                // 시작 시간이 종료 시간보다 늦은 경우 예외 처리
                if (!startTimeIsNULL && !endTimeIsNULL){
                    if (startTime.getHour() > endTime.getHour() ||
                            (startTime.getHour() == endTime.getHour() && startTime.getMinute() >= endTime.getMinute())) {
                        Toast.makeText(binding.getRoot().getContext(), "시작 시간이 종료 시간보다 늦습니다.\n다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        return;  // 현재 onClick 함수 종료
                    }
                }

                // 모든 값을 입력하지 않을 경우 예외 처리
                if (classTitle.isEmpty() || classPlace.isEmpty() || professorName.isEmpty() || endTimeIsNULL || startTimeIsNULL){
                    Toast.makeText(binding.getRoot().getContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    ArrayList<Schedule> scheduleList = new ArrayList<>();
                    Schedule schedule = new Schedule();
                    schedule.setClassTitle(classTitle);
                    schedule.setClassPlace(classPlace);
                    schedule.setProfessorName(professorName);
                    schedule.setStartTime(startTime);
                    schedule.setEndTime(endTime);
                    schedule.setDay(day);

                    scheduleList.add(schedule);

                    if (callback != null){
                        callback.onScheduleAdded(scheduleList);  // 어댑터의 콜백 호출 (거기 저장로직이 있음)
                    }

                    // 모든 작업이 끝나면 bottomSheetDialogFragment 닫기
                    dismiss();
                }


            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCallback(ScheduleCallback callback){
        this.callback = callback;
    }


}
