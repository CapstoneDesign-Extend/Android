package com.example.schoolproject.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.schoolproject.room.dao.TimetableDao;
import com.example.schoolproject.room.entity.TimetableEntity;

@Database(entities = {TimetableEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TimetableDao timetableDao();
}
