package com.example.schoolproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Board {
    private Long id;

    private String title; // 제목
    private String content; // 본문
    private String author; // 작성자

    private Member member;

    private int viewCnt; // 조회수

    private int likeCnt; // 좋아요 개수

    private String finalDate; // LocalDateTime을 String으로 변환

    private BoardKind boardKind; // 게시판 종류

    private List<Comment> comments;

    private List<File> files;


    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public BoardKind getBoardKind() {
        return boardKind;
    }

    public void setBoardKind(BoardKind boardKind) {
        this.boardKind = boardKind;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
