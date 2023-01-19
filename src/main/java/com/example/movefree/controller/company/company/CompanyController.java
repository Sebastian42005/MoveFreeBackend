package com.example.movefree.controller.company.company;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.requests.CompanyRequestDTO;
import com.example.movefree.database.company.requests.CompanyRequestRepository;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.role.Role;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Api(tags = "Company")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    //Repositories
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyRequestRepository companyRequestRepository;

    //set address and phone number of user
    @PutMapping("/edit")
    public CompanyDTO editCompany(@RequestBody PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) {
        String phoneNumber = company.getPhoneNumber();
        String address = company.getAddress();
        UserDTO userDTO = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        CompanyDTO companyDTO = userDTO.getCompany();
        if (address != null) companyDTO.setAddress(address);
        if (phoneNumber != null) companyDTO.setPhoneNumber(phoneNumber);
        return companyRepository.save(companyDTO);
    }

    //send request to become a company
    @PostMapping("/request")
    public ResponseEntity<CompanyRequestDTO> requestCompany(@RequestBody CompanyRequestDTO.Request message, Principal principal) {
        CompanyRequestDTO companyRequestDTO = new CompanyRequestDTO();
        //get user
        UserDTO userDTO = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        //check if user already is company
        if (userDTO.getRole().equals(Role.COMPANY))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Company");
        //check if user already sent request
        if (companyRequestRepository.findByUsername(principal.getName()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already sent request");
        //send request
        companyRequestDTO.setUsername(principal.getName());
        companyRequestDTO.setMessage(message.getMessage());
        return ResponseEntity.ok(companyRequestRepository.save(companyRequestDTO));
    }
}
