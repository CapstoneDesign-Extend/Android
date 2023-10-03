package com.example.schoolproject.model;

import java.util.List;

public class Board {
    private Long id;
    private String title; // 제목
    private String content; // 본문
    private Integer price; // Market 게시판용 가격 정보
    private String author; // 작성자
    private Member member; // 데이터 객체 호환성 문제로 다시 추가함(createBoard 할때 member 가 필요)
    private Long memberId;  // member's id
    private int viewCnt; // 조회수

    private int chatCnt; // 댓글수

    private int likeCnt; // 좋아요 개수

    private String finalDate; // LocalDateTime을 String으로 변환

    private BoardKind boardKind; // 게시판 종류
    private List<String> images; // 이미지 URL 목록

    private List<Comment> comments;

    private List<File> files;

    // additional fn
    public boolean hasImages(){
        return images != null && !images.isEmpty();
    }

    // getters and setters

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public int getChatCnt() {
        return chatCnt;
    }

    public void setChatCnt(int chatCnt) {
        this.chatCnt = chatCnt;
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
