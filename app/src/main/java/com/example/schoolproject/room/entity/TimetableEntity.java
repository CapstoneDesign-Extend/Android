package com.example.schoolproject.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimetableEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String jsonData;
}
