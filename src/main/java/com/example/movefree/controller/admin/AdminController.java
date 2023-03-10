package com.example.movefree.controller.admin;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.database.company.requests.CompanyRequestRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.role.Role;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Api(tags = "Admin")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    final CompanyRequestRepository companyRequestRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;

    public AdminController(CompanyRequestRepository companyRequestRepository, CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRequestRepository = companyRequestRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    //get all company requests
    @GetMapping("/company-requests")
    public List<CompanyRequest> getAllRequests() {
        return companyRequestRepository.findAll();
    }

    //decline company request
    @DeleteMapping("/company-requests/{id}/decline")
    public ResponseEntity<String> declineRequest(@PathVariable UUID id) {
        //get company request by id
        CompanyRequest companyRequest = companyRequestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        //delete company request
        companyRequestRepository.deleteById(id);
        //return message
        return ResponseEntity.ok("Declined Request from " + companyRequest.getUsername());
    }

    //accept company request
    @PatchMapping("/company-requests/{id}/accept")
    public ResponseEntity<String> acceptRequest(@PathVariable UUID id) {
        //get company request by id
        CompanyRequest companyRequest = companyRequestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        //get user from request
        User user = userRepository.findByUsername(companyRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        //create company
        Company company = companyRepository.save(new Company());
        //set role of user to company
        user.setRole(Role.COMPANY);
        //add company to user
        user.setCompany(company);
        //delete request
        companyRequestRepository.deleteById(id);

        return ResponseEntity.ok("Created Company for user " + user.getUsername());
    }
}
