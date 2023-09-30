package com.example.schoolproject.test;

import com.google.gson.Gson;

public class MyUtils {
    public static String toJsonString(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
