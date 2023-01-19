package com.example.movefree.controller.company.timetable;

import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.timetable.TimeTable;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.file.FileHandler;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Api(tags = "Company Timetable")
@RestController
@RequestMapping("/api/company/timetable")
public class TimetableController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;
    private final FileHandler<TimeTable> fileHandler = FileHandler.getInstance();

    //create a timetable for the company
    @PostMapping("/create")
    @SneakyThrows
    public ResponseEntity<String> createTimeTable(@RequestBody TimeTable timeTable, Principal principal) {
        UserDTO user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        fileHandler.writeFile(timeTable, "Company" + user.getCompany().getId());

        return ResponseEntity.ok("Success");
    }

    //get timetable of company
    @GetMapping("/{id}")
    public ResponseEntity<TimeTable> getTimeTable(@PathVariable int id) {
        TimeTable timeTable = fileHandler.toObject("Company" + id, TimeTable.class);
        if (timeTable == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Timetable not found");
        return ResponseEntity.ok(timeTable);
    }
}
