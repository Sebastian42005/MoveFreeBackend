package com.example.movefree.service.company;

import com.example.movefree.controller.company.timetable.TimetableRequest;
import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.timetable.course.Course;
import com.example.movefree.database.timetable.course.CourseDTO;
import com.example.movefree.database.timetable.course.CourseDTOMapper;
import com.example.movefree.database.timetable.course.CourseRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.enums.NotFoundType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableService {


    final CompanyRepository companyRepository;
    final CourseRepository courseRepository;

    public TimetableService(CompanyRepository companyRepository, CourseRepository courseRepository) {
        this.companyRepository = companyRepository;
        this.courseRepository = courseRepository;
    }

    
    public List<CourseDTO> createTimetable(List<TimetableRequest> courses, String name) throws IdNotFoundException{
        Company company = getCompany(name);
        List<Course> courseList = new ArrayList<>();
        courses.stream().map(course -> new Course(
                course.getName(),
                course.getDay(),
                course.getStart(),
                course.getEnd())).forEach(course -> courseList.add(courseRepository.save(course)));
        company.setTimetable(courseList);
        companyRepository.save(company);
        return courseList.stream().map(new CourseDTOMapper()).toList();
    }

    
    public List<CourseDTO> getTimeTable(String name) throws IdNotFoundException {
        Company company = getCompany(name);
        if (company.getTimetable() == null) throw new IdNotFoundException(NotFoundType.TIMETABLE);
        return company.getTimetable().stream().map(new CourseDTOMapper()).toList();
    }

    private Company getCompany(String name) throws IdNotFoundException {
        return companyRepository.findByName(name).orElseThrow(IdNotFoundException.get(NotFoundType.COMPANY));
    }
}
