package com.example.movefree.database.timetable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum CourseDay {
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
    SUNDAY("sunday");

    private final String value;

    CourseDay(String value) { this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public CourseDay fromValue() {
        for (CourseDay courseDay : CourseDay.values()) {
            if (courseDay.value.equals(value.toLowerCase(Locale.ROOT).trim())) return courseDay;
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid day");
    }
}
