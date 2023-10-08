package com.example.schoolproject.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.schoolproject.room.entity.TimetableEntity;

@Dao
public interface TimetableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimetable(TimetableEntity timetable);

    @Query("SELECT * FROM TimetableEntity WHERE id = :id")
    TimetableEntity getTimetableById(int id);
}
