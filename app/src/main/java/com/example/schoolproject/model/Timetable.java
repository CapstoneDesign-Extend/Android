package com.example.schoolproject.model;

public class Timetable {
    private Long id;
    private String day; // 요일
    private int schedule_year; // 연도
    private int semester; // 학기 => 1학기 or 2학기
    private String schedule; // 일정 => 저장할 스케쥴
    private Member member; // 한 명의 사용자는 여러 시간표를 가질 수 있음

}
