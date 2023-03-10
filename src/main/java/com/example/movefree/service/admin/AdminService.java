package com.example.movefree.service.admin;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.database.company.requests.CompanyRequestRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.port.admin.AdminPort;
import com.example.movefree.role.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService implements AdminPort {

    final CompanyRequestRepository companyRequestRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;

    public AdminService(CompanyRequestRepository companyRequestRepository, CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRequestRepository = companyRequestRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CompanyRequest> getAllRequests() {
        return companyRequestRepository.findAll();
    }

    @Override
    public String declineRequest(UUID id) throws IdNotFoundException {
        CompanyRequest companyRequest = companyRequestRepository.findById(id).orElseThrow(() -> new IdNotFoundException(NotFoundType.USER));
        companyRequestRepository.deleteById(id);
        return "Declined Request from " + companyRequest.getUsername();
    }

    @Override
    public String acceptRequest(UUID id) throws IdNotFoundException {
        CompanyRequest companyRequest;
        companyRequest = companyRequestRepository.findById(id).orElseThrow(() -> new IdNotFoundException(NotFoundType.COMPANY_REQUEST));
        User user = userRepository.findByUsername(companyRequest.getUsername()).orElseThrow(() -> new IdNotFoundException(NotFoundType.USER));
        Company company = companyRepository.save(new Company());
        user.setRole(Role.COMPANY);
        user.setCompany(company);
        companyRequestRepository.deleteById(id);

        return "Created Company for user " + user.getUsername();
    }
}
