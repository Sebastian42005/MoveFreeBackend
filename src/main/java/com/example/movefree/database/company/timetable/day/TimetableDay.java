package com.example.movefree.database.company.timetable.day;

import com.example.movefree.database.company.timetable.timetable.Timetable;
import com.example.movefree.database.company.timetable.event.TimetableEvent;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "timetable_days")
public class TimetableDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonManagedReference("timetable_event_days")
    @OneToMany(mappedBy = "day")
    private List<TimetableEvent> events;

    @JsonBackReference("timetable_days")
    @ManyToOne
    private Timetable timetable;
}
