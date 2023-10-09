package com.example.schoolproject.timetableview;

import com.github.tlaabs.timetableview.Time;

import java.util.Locale;

public class ConvertFormat {
    public static String convertDay(int day){
        switch (day) {
            case 0: return "월";
            case 1: return "화";
            case 2: return "수";
            case 3: return "목";
            case 4: return "금";
            default: return "?";
        }
    }
    public static int convertDay(String dayKor){
        switch (dayKor){
            case "월요일": return 0;
            case "화요일": return 1;
            case "수요일": return 2;
            case "목요일": return 3;
            case "금요일": return 4;
            default: return -1;
        }
    }
    public static String convertTime(Time time){
        String timeString = String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinute());
        return timeString;
    }
}
