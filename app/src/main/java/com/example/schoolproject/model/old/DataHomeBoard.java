package com.example.schoolproject.model.old;


import java.util.List;

public class DataHomeBoard {

    private String board_name;
    private List<String> post_titles;
    private List<String> post_contents;
    private List<String> post_ids;
    public DataHomeBoard(){}

    public DataHomeBoard(String board_name, List<String> post_titles, List<String> post_contents, List<String> post_ids){
        this.board_name = board_name;
        this.post_titles = post_titles;
        this.post_contents = post_contents;
        this.post_ids = post_ids;
    }

    // getters and setters

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    public List<String> getPost_titles() {
        return post_titles;
    }

    public void setPost_titles(List<String> post_titles) {
        this.post_titles = post_titles;
    }

    public List<String> getPost_contents() {
        return post_contents;
    }

    public void setPost_contents(List<String> post_contents) {
        this.post_contents = post_contents;
    }

    public List<String> getPost_ids() {
        return post_ids;
    }

    public void setPost_ids(List<String> post_ids) {
        this.post_ids = post_ids;
    }
}
