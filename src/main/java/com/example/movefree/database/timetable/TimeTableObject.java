package com.example.movefree.database.timetable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeTableObject {
    String from;
    String to;
    String name;
}
