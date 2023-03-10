package com.example.movefree.port.company;

import com.example.movefree.database.timetable.TimeTable;
import com.example.movefree.exception.IdNotFoundException;

import java.util.UUID;

public interface TimetablePort {
    void createTimetable(TimeTable timeTable, String name) throws IdNotFoundException;
    TimeTable getTimeTable(UUID id) throws IdNotFoundException;
}
