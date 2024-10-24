package com.example.movefree.port.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface CompanyMemberPort {
    CompanyMemberDTO createMember(String member, Principal principal) throws IdNotFoundException;
    Picture setProfilePicture(Integer id, MultipartFile image, Principal principal) throws UserForbiddenException, IdNotFoundException, IOException;
    Picture getProfilePicture(Integer id) throws IdNotFoundException;
}
