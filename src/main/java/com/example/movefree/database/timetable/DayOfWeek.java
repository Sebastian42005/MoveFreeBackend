package com.example.movefree.database.timetable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum DayOfWeek {
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
    SUNDAY("sunday");

    private final String value;

    DayOfWeek(String value) { this.value = value; }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public DayOfWeek fromValue() {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek.value.equals(value.toLowerCase(Locale.ROOT).trim())) return dayOfWeek;
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid day");
    }
}
