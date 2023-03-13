package com.example.movefree.port.company;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CompanyPort {

    CompanyDTO editCompany(PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) throws IdNotFoundException;

    void uploadPost(Principal principal, List<MultipartFile> images, String description) throws InvalidMultipartFileException, IdNotFoundException;

    Picture getPicture(UUID id) throws IdNotFoundException;

}
