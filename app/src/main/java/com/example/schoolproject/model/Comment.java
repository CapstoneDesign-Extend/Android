package com.example.schoolproject.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Comment {
    @SerializedName("comment_id")
    private Long id;

    private Board board;  // 게시판 id를 가져오기위해

    private String content; // 본문

    @SerializedName("finalDate")
    private LocalDateTime finalDate;

    @SerializedName("count") // 변수명 변경
    private int count; // 좋아요 갯수

    @SerializedName("member") // 변수명 변경 및 연관된 객체로 변환
    private Member member;
}