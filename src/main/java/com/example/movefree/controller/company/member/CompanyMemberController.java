package com.example.movefree.controller.company.member;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.database.company.member.member.CompanyMemberRepository;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.security.Principal;

@Api(tags = "Company Member")
@RestController
@RequestMapping("/api/company/members")
public class CompanyMemberController {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyMemberRepository memberRepository;

    @PostMapping("/create")
    public CompanyMemberDTO createMember(@RequestBody CompanyMemberDTO memberDTO, Principal principal) {
        UserDTO userDTO = getUser(principal.getName());
        CompanyDTO companyDTO = userDTO.getCompany();
        memberDTO.setCompany(companyDTO);
        CompanyMemberDTO savedMember = memberRepository.save(memberDTO);
        companyDTO.getMembers().add(savedMember);
        return savedMember;
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<byte[]> setProfilePicture(@PathVariable int id, @RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        checkForAuthorization(principal.getName(), id);
        CompanyMemberDTO companyMemberDTO = getCompanyMember(id);
        companyMemberDTO.setProfilePicture(image.getBytes());
        companyMemberDTO.setContentType(image.getContentType());
        memberRepository.save(companyMemberDTO);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(companyMemberDTO.getContentType()))
                .body(companyMemberDTO.getProfilePicture());
    }

    //get profile picture of company member
    @GetMapping("/{id}/profile")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable int id) {
        CompanyMemberDTO companyMemberDTO = getCompanyMember(id);
        if (companyMemberDTO.getProfilePicture() == null) return null;
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(companyMemberDTO.getContentType()))
                .body(companyMemberDTO.getProfilePicture());
    }
    private void checkForAuthorization(String username, int memberId) {
        UserDTO userDTO = getUser(username);
        if (userDTO.getCompany().getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private UserDTO getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private CompanyMemberDTO getCompanyMember(int id) {
        return memberRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    }
}
