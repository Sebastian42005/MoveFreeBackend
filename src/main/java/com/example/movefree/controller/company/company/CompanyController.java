package com.example.movefree.controller.company.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyDTOMapper;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.database.company.requests.CompanyRequestRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.role.Role;
import io.swagger.annotations.Api;
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
    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final CompanyRequestRepository companyRequestRepository;

    CompanyDTOMapper companyDTOMapper = new CompanyDTOMapper();

    public CompanyController(CompanyRepository companyRepository, UserRepository userRepository, CompanyRequestRepository companyRequestRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyRequestRepository = companyRequestRepository;
    }

    //set address and phone number of user
    @PutMapping("/edit")
    public CompanyDTO editCompany(@RequestBody PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) {
        String phoneNumber = company.getPhoneNumber();
        String address = company.getAddress();
        User userDTO = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Company userCompany = userDTO.getCompany();
        if (address != null) userCompany.setAddress(address);
        if (phoneNumber != null) userCompany.setPhoneNumber(phoneNumber);
        return companyDTOMapper.apply(companyRepository.save(userCompany));
    }

    //send request to become a company
    @PostMapping("/request")
    public ResponseEntity<CompanyRequest> requestCompany(@RequestBody CompanyRequest.Request message, Principal principal) {
        CompanyRequest companyRequest = new CompanyRequest();
        //get user
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        //check if user already is company
        if (user.getRole().equals(Role.COMPANY))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Company");
        //check if user already sent request
        if (companyRequestRepository.findByUsername(principal.getName()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already sent request");
        //send request
        companyRequest.setUsername(principal.getName());
        companyRequest.setMessage(message.getMessage());
        return ResponseEntity.ok(companyRequestRepository.save(companyRequest));
    }
}
