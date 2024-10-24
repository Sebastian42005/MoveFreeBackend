package com.example.movefree.controller.company.post;

import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.port.company.CompanyPort;
import com.example.movefree.portclass.Picture;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Api(tags = "Company Post Controller")
@RestController
@RequestMapping("/api/company/post")
public class CompanyPostController {

    final CompanyPort companyPort;

    public CompanyPostController(CompanyPort companyPort) {
        this.companyPort = companyPort;
    }

    /**
     * 200 - Success
     * 403 - User is forbidden
     * 404 - Company not found
     * 415 - Invalid file type
     */
    @PostMapping
    public ResponseEntity<String> createCompanyPost(@RequestParam("description") String description,
                                                    @RequestParam("images") List<MultipartFile> images, Principal principal) {
        try {
            companyPort.uploadPost(principal, images, description);
            return ResponseEntity.ok("Success");
        } catch (InvalidMultipartFileException | IdNotFoundException e) {
            return e.getResponseEntityWithMessage();
        }
    }

    /**
     * 200 - Success
     * 404 - Picture not found
     */
    @GetMapping("/picture/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable("id") Integer id) {
        try {
            Picture picture = companyPort.getPicture(id);
            return ResponseEntity.ok()
                    .contentType(picture.contentType())
                    .body(picture.content());
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
