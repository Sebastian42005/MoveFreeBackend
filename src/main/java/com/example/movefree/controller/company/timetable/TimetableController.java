package com.example.movefree.controller.company.timetable;

import com.example.movefree.database.timetable.TimeTable;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.port.company.TimetablePort;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(tags = "Company Timetable")
@RestController
@RequestMapping("/api/company/timetable")
public class TimetableController {

    final TimetablePort timetablePort;

    public TimetableController(TimetablePort timetablePort) {
        this.timetablePort = timetablePort;
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @PostMapping("/create")
    @SneakyThrows
    public ResponseEntity<Object> createTimeTable(@RequestBody TimeTable timeTable, Principal principal) {
        try {
            timetablePort.createTimetable(timeTable, principal.getName());
            return ResponseEntity.ok().build();
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Timetable not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimeTable> getTimeTable(@PathVariable int id) {
        try {
            return ResponseEntity.ok(timetablePort.getTimeTable(id));
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
