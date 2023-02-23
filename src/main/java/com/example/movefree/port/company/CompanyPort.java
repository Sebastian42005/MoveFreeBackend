package com.example.movefree.port.company;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.exception.AlreadyCompanyException;
import com.example.movefree.exception.AlreadySentRequestException;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.exception.NoCompanyException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface CompanyPort {

    CompanyDTO editCompany(PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) throws UserForbiddenException, NoCompanyException;
    CompanyRequest requestCompany(CompanyRequest.Request message, Principal principal) throws UserForbiddenException, AlreadyCompanyException, AlreadySentRequestException;

    void uploadPost(Principal principal, List<MultipartFile> images, String description) throws UserForbiddenException, InvalidMultipartFileException, NoCompanyException;

    Picture getPicture(int id) throws IdNotFoundException;

}
