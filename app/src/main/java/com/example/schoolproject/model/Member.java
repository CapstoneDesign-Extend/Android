package com.example.schoolproject.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Member implements Parcelable {
    @SerializedName("id")
    private Long id;

    @SerializedName("studentId")
    private int studentId;

    @SerializedName("name")
    private String name;

    @SerializedName("schoolName")
    private String schoolName;

    @SerializedName("access")
    private Access access;

    @SerializedName("loginId")
    private String loginId;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("department")
    private String department;

    // 생성자, Getter 및 Setter


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Parcelable 관련 메서드
    public Member(Parcel in) {
        id = in.readLong();
        studentId = in.readInt();
        name = in.readString();
        schoolName = in.readString();
        access = Access.valueOf(in.readString());
        loginId = in.readString();
        password = in.readString();
        email = in.readString();
    }
    public Member(){};

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(studentId);
        dest.writeString(name);
        dest.writeString(schoolName);
        dest.writeString(String.valueOf(access));
        dest.writeString(loginId);
        dest.writeString(password);
        dest.writeString(email);
    }
}