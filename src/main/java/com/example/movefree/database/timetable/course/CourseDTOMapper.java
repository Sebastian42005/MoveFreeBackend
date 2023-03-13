package com.example.movefree.database.timetable.course;


import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class CourseDTOMapper implements Function<Course, CourseDTO> {
    @Override
    public CourseDTO apply(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getDay(),
                course.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                course.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
