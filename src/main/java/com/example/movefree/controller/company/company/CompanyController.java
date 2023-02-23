package com.example.movefree.controller.company.company;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.exception.AlreadyCompanyException;
import com.example.movefree.exception.AlreadySentRequestException;
import com.example.movefree.exception.NoCompanyException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.port.company.CompanyPort;
import com.example.movefree.request_body.PostSpotRequestBody;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(tags = "Company")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    final CompanyPort companyPort;

    public CompanyController(CompanyPort companyPort) {
        this.companyPort = companyPort;
    }

    //set address and phone number of user
    @PutMapping("/edit")
    public ResponseEntity<CompanyDTO> editCompany(@RequestBody PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) {
        try {
            return ResponseEntity.ok(companyPort.editCompany(company, principal));
        } catch (UserForbiddenException | NoCompanyException e) {
            return e.getResponseEntity();
        }
    }

    //send request to become a company
    @PostMapping("/request")
    public ResponseEntity<CompanyRequest> requestCompany(@RequestBody CompanyRequest.Request message, Principal principal) {
        try {
            return ResponseEntity.ok(companyPort.requestCompany(message, principal));
        } catch (UserForbiddenException | AlreadyCompanyException | AlreadySentRequestException e) {
            return e.getResponseEntity();
        }
    }
}
