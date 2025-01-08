package com.example.movefree.controller.company.company;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.service.company.CompanyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Api(tags = "Company")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {

    final CompanyService companyService;

    /**
     * 200 - Success
     * 403 - User not found
     * 403 - User is not a company
     */
    @PutMapping("/edit")
    public ResponseEntity<CompanyDTO> editCompany(@RequestBody PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) {
        try {
            return ResponseEntity.ok(companyService.editCompany(company, principal));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
