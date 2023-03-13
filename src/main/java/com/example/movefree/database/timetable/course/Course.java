package com.example.movefree.database.timetable.course;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.timetable.CourseDay;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    public Course(String name, CourseDay day, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    String name;
    CourseDay day;

    LocalTime startTime;
    LocalTime endTime;

    @JsonBackReference("company_timetable")
    @ManyToOne
    Company company;
}
