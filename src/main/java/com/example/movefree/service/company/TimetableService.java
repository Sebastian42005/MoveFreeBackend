package com.example.movefree.service.company;

import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.timetable.TimeTable;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.enums.enums.NotFoundType;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.file.FileHandler;
import com.example.movefree.port.company.TimetablePort;
import org.springframework.stereotype.Service;

@Service
public class TimetableService implements TimetablePort {

    final CompanyRepository companyRepository;

    final UserRepository userRepository;

    private final FileHandler<TimeTable> fileHandler = FileHandler.getInstance();

    public TimetableService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createTimetable(TimeTable timeTable, String name) throws IdNotFoundException{
        User user = userRepository.findByUsername(name).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
        fileHandler.writeFile(timeTable, "Company" + user.getCompany().getId());
    }

    @Override
    public TimeTable getTimeTable(int id) throws IdNotFoundException {
        TimeTable timeTable = fileHandler.toObject("Company" + id, TimeTable.class);
        if (timeTable == null) throw new IdNotFoundException(NotFoundType.TIMETABLE);
        return timeTable;
    }
}
