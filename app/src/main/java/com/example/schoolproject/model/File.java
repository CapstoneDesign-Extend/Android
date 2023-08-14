package com.example.schoolproject.model;

public class File {
    private Long id; // 개별 아이디

    private String fileName; // 파일 이름

    private long fileSize; // 파일 사이즈

    private String fileType; // 파일 타입
    private byte[] fileData; // 파일에 대한 데이터로 파일의 내용을 바이트 배열로 변환한 데이터가 저장됨
    private Board board;
}
