package com.example.movefree.database.timetable.course;

import com.example.movefree.database.timetable.CourseDay;

import java.util.UUID;

public record CourseDTO(
        UUID id,
        String name,
        CourseDay day,
        String start,
        String end) {}
