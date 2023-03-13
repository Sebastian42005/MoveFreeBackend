package com.example.movefree.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotFoundType {
    USER("User"),
    COMPANY("Company"),
    ROLE("Role"),
    MEMBER("Member"),
    SPOT("Spot"),
    PICTURE("Picture"),
    TIMETABLE("Timetable");

    private final String name;
}
