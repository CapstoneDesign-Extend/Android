package com.example.schoolproject.nav.gp;


import com.github.tlaabs.timetableview.Schedule;

import java.util.ArrayList;

public interface ScheduleCallback {
    void onScheduleAdded(ArrayList<Schedule> schedules);
}
