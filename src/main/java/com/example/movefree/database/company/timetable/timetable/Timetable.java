package com.example.movefree.database.company.timetable.timetable;


import com.example.movefree.database.company.timetable.day.TimetableDay;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "timetables")
public class Timetable {

    @Id
    private int id;

    @JsonManagedReference("timetable_days")
    @OneToMany(mappedBy = "timetable")
    private List<TimetableDay> days;

}