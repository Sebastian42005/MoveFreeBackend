package com.example.movefree.controller.admin;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.requests.CompanyRequestDTO;
import com.example.movefree.database.company.requests.CompanyRequestRepository;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.role.Role;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(tags = "Admin")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    CompanyRequestRepository companyRequestRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;

    //get all company requests
    @GetMapping("/company-requests")
    public List<CompanyRequestDTO> getAllRequests() {
        return companyRequestRepository.findAll();
    }

    //decline company request
    @DeleteMapping("/company-requests/{id}/decline")
    public ResponseEntity<String> declineRequest(@PathVariable int id) {
        //get company request by id
        CompanyRequestDTO companyRequestDTO = companyRequestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        //delete company request
        companyRequestRepository.deleteById(id);
        //return message
        return ResponseEntity.ok("Declined Request from " + companyRequestDTO.getUsername());
    }

    //accept company request
    @PatchMapping("/company-requests/{id}/accept")
    public ResponseEntity<String> acceptRequest(@PathVariable int id) {
        //get company request by id
        CompanyRequestDTO companyRequestDTO = companyRequestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        //get user from request
        UserDTO userDTO = userRepository.findByUsername(companyRequestDTO.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        //create company
        CompanyDTO companyDTO = companyRepository.save(new CompanyDTO());
        //set role of user to company
        userDTO.setRole(Role.COMPANY);
        //add company to user
        userDTO.setCompany(companyDTO);
        //delete request
        companyRequestRepository.deleteById(id);

        return ResponseEntity.ok("Created Company for user " + userDTO.getUsername());
    }
}
