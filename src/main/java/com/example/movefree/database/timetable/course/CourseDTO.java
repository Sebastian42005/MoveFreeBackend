package com.example.movefree.database.timetable.course;

import com.example.movefree.database.timetable.DayOfWeek;

public record CourseDTO(
        Integer id,
        String name,
        DayOfWeek day,
        String start,
        String end) {}
