package com.example.movefree.controller.company.timetable;

import com.example.movefree.database.timetable.DayOfWeek;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class TimetableRequest {
    String name;
    DayOfWeek day;

    LocalTime start;
    LocalTime end;
}
