package com.example.schoolproject.model.ui;

public class DataHomeDynamicMorning {
    private String title;
    private String lecture1;
    private String lecture2;

    public DataHomeDynamicMorning(String title, String lecture1, String lecture2){
        this.title = title;
        this.lecture1 = lecture1;
        this.lecture2 = lecture2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLecture1() {
        return lecture1;
    }

    public void setLecture1(String lecture1) {
        this.lecture1 = lecture1;
    }

    public String getLecture2() {
        return lecture2;
    }

    public void setLecture2(String lecture2) {
        this.lecture2 = lecture2;
    }
}
