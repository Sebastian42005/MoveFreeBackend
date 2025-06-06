package com.example.movefree.controller.company.member;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.portclass.Picture;
import com.example.movefree.service.company.CompanyMemberService;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Api(tags = "Company Member")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company/members")
public class CompanyMemberController {

    final CompanyMemberService companyMemberService;
    
    @NoArgsConstructor
    @Getter
    private static class CompanyMemberRequest {String name;}

    /**
     * 200 - Success
     * 403 - User is forbidden
     */
    @PostMapping("/create")
    public ResponseEntity<CompanyMemberDTO> createMember(@RequestBody CompanyMemberRequest companyMemberRequest, Principal principal) {
        try {
            return ResponseEntity.ok(companyMemberService.createMember(companyMemberRequest.name, principal));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Member not found
     */
    @PutMapping("/{id}/profile")
    public ResponseEntity<byte[]> setProfilePicture(@PathVariable Integer id, @RequestParam("image") MultipartFile image, Principal principal) {
        try {
            Picture picture = companyMemberService.setProfilePicture(id, image, principal);
            return ResponseEntity.ok()
                    .contentType(picture.contentType())
                    .body(picture.content());
        } catch (IdNotFoundException | UserForbiddenException e) {
            return e.getResponseEntity();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 200 - Success
     * 404 - Member not found
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Integer id,
                                                    @RequestParam(defaultValue = "false") boolean dark) {
        try {
            Picture picture = companyMemberService.getProfilePicture(id, dark);
            return ResponseEntity.ok()
                    .contentType(picture.contentType())
                    .body(picture.content());
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
