package com.example.movefree.port.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

public interface CompanyMemberPort {
    CompanyMemberDTO createMember(String member, Principal principal) throws IdNotFoundException;
    Picture setProfilePicture(UUID id, MultipartFile image, Principal principal) throws UserForbiddenException, IdNotFoundException, IOException;
    Picture getProfilePicture(UUID id) throws IdNotFoundException;
}
