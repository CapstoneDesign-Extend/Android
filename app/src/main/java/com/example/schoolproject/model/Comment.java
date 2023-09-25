package com.example.schoolproject.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;

    //private Board board;  // 게시판 id를 가져오기위해

    private String content; // 본문

    private String finalDate;

    private int likeCount; // 좋아요 갯수

    //private Member member;
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }





    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}