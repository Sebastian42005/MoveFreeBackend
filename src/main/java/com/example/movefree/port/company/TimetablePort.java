package com.example.movefree.port.company;

import com.example.movefree.controller.company.timetable.TimetableRequest;
import com.example.movefree.database.timetable.course.CourseDTO;
import com.example.movefree.exception.IdNotFoundException;

import java.util.List;

public interface TimetablePort {
    List<CourseDTO> createTimetable(List<TimetableRequest> courses, String name) throws IdNotFoundException;
    List<CourseDTO> getTimeTable(String name) throws IdNotFoundException;
}
