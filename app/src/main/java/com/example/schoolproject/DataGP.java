package com.example.schoolproject;

public class DataGP {
    private String gpa;
    private String credit;

    public DataGP(String gpa, String credit) {
        this.gpa = gpa;
        this.credit = credit;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
