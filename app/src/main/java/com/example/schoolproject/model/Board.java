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

    @SerializedName("boardKind") // 변수명 변경
    private BoardKind boardKind; // 게시판 종류

    private List<Comment> comments;

    private List<File> files;

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getViewcnt() {
        return viewcnt;
    }

    public void setViewcnt(int viewcnt) {
        this.viewcnt = viewcnt;
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
