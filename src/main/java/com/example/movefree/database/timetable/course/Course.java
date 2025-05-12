package com.example.movefree.database.timetable.course;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.timetable.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    public Course(String name, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String name;
    DayOfWeek day;

    LocalTime startTime;
    LocalTime endTime;

    @JsonBackReference("company_timetable")
    @ManyToOne
    @JsonIgnore
    Company company;
}
