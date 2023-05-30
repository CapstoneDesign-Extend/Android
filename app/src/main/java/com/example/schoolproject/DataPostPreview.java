package com.example.schoolproject;

public class DataPostPreview {
    private int imageResourceId;
    private String post_title;
    private String post_content;
    private String heart_count;
    private String chat_count;
    private String time;
    private String auth;

    public DataPostPreview(int imageResourceId, String post_title, String post_content, String heart_count, String chat_count, String time, String auth) {
        this.imageResourceId = imageResourceId;
        this.post_title = post_title;
        this.post_content = post_content;
        this.heart_count = heart_count;
        this.chat_count = chat_count;
        this.time = time;
        this.auth = auth;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
