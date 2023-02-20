package com.example.movefree.port.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.NoCompanyException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface CompanyMemberPort {
    CompanyMemberDTO createMember(String member, Principal principal) throws UserForbiddenException;
    Picture setProfilePicture(int id, MultipartFile image, Principal principal) throws NoCompanyException, UserForbiddenException, IdNotFoundException, IOException;
    Picture getProfilePicture(int id) throws IdNotFoundException;
}
