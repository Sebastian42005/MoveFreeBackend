package com.example.movefree.database.timetable.course;

import com.example.movefree.database.timetable.CourseDay;

public record CourseDTO(
        Integer id,
        String name,
        CourseDay day,
        String start,
        String end) {}
