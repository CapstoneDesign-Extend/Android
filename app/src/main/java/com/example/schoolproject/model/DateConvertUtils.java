package com.example.schoolproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertUtils {
    public static String convertDate(String mysqlDate, String format){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try{
            Date dateMysql = inputFormat.parse(mysqlDate);
            String formattedDate = dateFormat.format(dateMysql);
            String formattedTime = timeFormat.format(dateMysql);
            if (format.equals("date")){return formattedDate;}
            if (format.equals("time")){return formattedTime;}

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return "error:convertFailed";
    }
}
