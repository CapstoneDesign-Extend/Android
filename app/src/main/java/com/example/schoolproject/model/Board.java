package com.example.schoolproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Board {
    @SerializedName("board_id")
    private Long id;

    private String title; // 제목
    private String content; // 본문

    @SerializedName("member") // 변수명 변경 및 연관된 객체로 변환
    private Member member;

    @SerializedName("view_count")
    private int viewcnt; // 조회수

    @SerializedName("finalDate") // 변수명 변경 및 LocalDateTime을 String으로 변환
    private String finalDate;

    @SerializedName("kind") // 변수명 변경
    private Kind kind; // 게시판 종류

    private List<Comment> comments;

    private List<File> files;
}
