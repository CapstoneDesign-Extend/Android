package com.example.schoolproject;

public class DataPost {
    private int imageResourceId;
    private int postId;
    private String boardType;
    private String userId;
    private String title;
    private String content;
    private String author;
    private String date;
    private String time;
    private String heart_count;
    private String chat_count;
    // default
    public DataPost(){

    };

    // write post
    public DataPost(int imageResourceId, String title, String content, String author, String date, String time) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.time = time;
    }
    // preview post
    public DataPost(int imageResourceId, String title, String content, String author, String time, String heart_count, String chat_count) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.content = content;
        this.time = time;
        this.author = author;
        this.heart_count = heart_count;
        this.chat_count = chat_count;
    }
    // post
    public DataPost(int imageResourceId, String title, String content, String date, String time, String author, String heart_count, String chat_count) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.time = time;
        this.heart_count = heart_count;
        this.chat_count = chat_count;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeart_count() {
        return heart_count;
    }

    public void setHeart_count(String heart_count) {
        this.heart_count = heart_count;
    }

    public String getChat_count() {
        return chat_count;
    }

    public void setChat_count(String chat_count) {
        this.chat_count = chat_count;
    }
}
