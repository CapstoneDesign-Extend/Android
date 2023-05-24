package com.example.schoolproject;


import java.util.List;

public class DataHomeBoard {

    private String title;
    private List<String> names;
    private List<String> datas;

    public DataHomeBoard(String title, List<String> names, List<String> datas){
        this.title = title;
        this.names = names;
        this.datas = datas;
    }

    // getters and setters


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }
}
