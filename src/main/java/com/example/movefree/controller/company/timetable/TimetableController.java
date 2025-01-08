package com.example.movefree.controller.company.timetable;

import com.example.movefree.database.timetable.course.CourseDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.service.company.TimetableService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Api(tags = "Company Timetable")
@RestController
@RequestMapping("/api/company/timetable")
public class TimetableController {

    final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    /**
     * 200 - Success
     * 404 - Company not found
     */
    @PostMapping
    public ResponseEntity<List<CourseDTO>> createTimeTable(@RequestBody List<TimetableRequest> courses, Principal principal) {
        try {
            return ResponseEntity.ok(timetableService.createTimetable(courses, principal.getName()));
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Timetable not found
     */
    @GetMapping("/{name}")
    public ResponseEntity<List<CourseDTO>> getTimeTable(@PathVariable String name) {
        try {
            return ResponseEntity.ok(timetableService.getTimeTable(name));
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
