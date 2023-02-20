package com.example.movefree.database.company.timetable.event;

import com.example.movefree.database.company.timetable.day.TimetableDay;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "timetable_events")
public class TimetableEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String eventStart;
    private String eventEnd;

    private String description;

    @JsonBackReference("timetable_event_days")
    @ManyToOne
    private TimetableDay day;
}
