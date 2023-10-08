package com.example.schoolproject.nav.gp;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.schoolproject.MainActivity;
import com.example.schoolproject.R;

import io.reactivex.rxjava3.annotations.NonNull;

public class TimePickerPopupDialogTwoButton extends Dialog {
    private Context context;
    private TimePickerPopupDialogClickListener TimePickerPopupDialogClickListener;
    private TimePicker timePicker;
    private TextView tvTitle, tvNegative, tvPositive;
    private String text;
    private String title;
    // custom
    private int setHourValue;
    private int setMinuteVale;

    public TimePickerPopupDialogTwoButton(@NonNull Context context,
                                          TimePickerPopupDialogClickListener TimePickerPopupDialogClickListener){
        super(context);
        this.context = context;
        this.TimePickerPopupDialogClickListener = TimePickerPopupDialogClickListener;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSetHourValue(int setHourValue) {
        this.setHourValue = setHourValue;
    }

    public void setSetMinuteVale(int setMinuteVale) {
        this.setMinuteVale = setMinuteVale;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker_alert_dialog_two);

        // Add this code to set the dialog's background to be rounded
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.drawable.shape_round_border);
        }


        timePicker = (TimePicker) findViewById(R.id.timepicker_alert_two);

        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(setHourValue);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setMinute(setMinuteVale);
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                setHourValue = hourOfDay;
                setMinuteVale = minute;
                Log.d(TAG, setHourValue + "시" + setMinuteVale + "분");
            }
        });
        tvPositive = findViewById(R.id.time_btn_yes);
        tvPositive.setOnClickListener(v -> {
            // if yes btn clicked
            this.TimePickerPopupDialogClickListener.onPositiveClick(setHourValue, setMinuteVale);
            dismiss();;
        });
        tvNegative = findViewById(R.id.time_btn_no);
        tvNegative.setOnClickListener(v -> {
            // if yes btn clicked
            this.TimePickerPopupDialogClickListener.onNegativeClick();
            dismiss();;
        });
    }
}
