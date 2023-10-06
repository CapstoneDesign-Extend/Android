package com.example.schoolproject.timetableview;

import com.github.tlaabs.timetableview.Time;

import java.io.Serializable;

public class Schedule implements Serializable {
    static final int MON = 0;
    static final int TUE = 1;
    static final int WED = 2;
    static final int THU = 3;
    static final int FRI = 4;
    static final int SAT = 5;
    static final int SUN = 6;

    String classTitle="";
    String classPlace="";
    String professorName="";
    private int day = 0;
    private com.github.tlaabs.timetableview.Time startTime;
    private com.github.tlaabs.timetableview.Time endTime;

    public Schedule() {
        this.startTime = new com.github.tlaabs.timetableview.Time();
        this.endTime = new com.github.tlaabs.timetableview.Time();
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public com.github.tlaabs.timetableview.Time getStartTime() {
        return startTime;
    }

    public void setStartTime(com.github.tlaabs.timetableview.Time startTime) {
        this.startTime = startTime;
    }

    public com.github.tlaabs.timetableview.Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
