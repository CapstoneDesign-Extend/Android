package com.example.schoolproject.model;

public class Notification {
    private Long id;
    private Member member; // 한 명의 사용자는 여러 알림을 받음
    private String content; // 알림 내용

}
