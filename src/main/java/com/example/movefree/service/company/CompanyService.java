package com.example.movefree.service.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyDTOMapper;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.post.CompanyPost;
import com.example.movefree.database.company.post.CompanyPostRepository;
import com.example.movefree.database.company.post.picture.CompanyPostPicture;
import com.example.movefree.database.company.post.picture.CompanyPostPictureRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.exception.enums.MultipartFileExceptionType;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.port.company.CompanyPort;
import com.example.movefree.portclass.Picture;
import com.example.movefree.request_body.PostSpotRequestBody;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyService implements CompanyPort {

    final CompanyRepository companyRepository;
    final CompanyPostPictureRepository companyPostPictureRepository;
    final CompanyPostRepository companyPostRepository;

    final CompanyDTOMapper companyDTOMapper = new CompanyDTOMapper();

    public CompanyService(CompanyRepository companyRepository,
                          CompanyPostPictureRepository companyPostPictureRepository,
                          CompanyPostRepository companyPostRepository) {
        this.companyRepository = companyRepository;
        this.companyPostPictureRepository = companyPostPictureRepository;
        this.companyPostRepository = companyPostRepository;
    }

    @Override
    public CompanyDTO editCompany(PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) throws IdNotFoundException {
        String phoneNumber = company.getPhoneNumber();
        String address = company.getAddress();
        Company userCompany = getCompany(principal);
        if (address != null) userCompany.setAddress(address);
        if (phoneNumber != null) userCompany.setPhoneNumber(phoneNumber);
        return companyDTOMapper.apply(companyRepository.save(userCompany));
    }

    @Override
    public void uploadPost(Principal principal, List<MultipartFile> pictures, String description) throws InvalidMultipartFileException, IdNotFoundException {
        Company company = getCompany(principal);
        CompanyPost companyPost = new CompanyPost();
        companyPost.setCompany(company);
        companyPost.setDescription(description);
        for (MultipartFile picture : pictures) {
            try {
                CompanyPostPicture companyPostPicture = new CompanyPostPicture();
                companyPostPicture.setContent(picture.getBytes());
                companyPostPicture.setContentType(picture.getContentType());
                companyPostPicture.setPost(companyPostRepository.save(companyPost));
                companyPostPictureRepository.save(companyPostPicture);
                return;
            } catch (IOException e) {
                throw new InvalidMultipartFileException(MultipartFileExceptionType.NO_CONTENT);
            }
        }
    }

    @Override
    public Picture getPicture(UUID id) throws IdNotFoundException {
        CompanyPostPicture companyPostPicture = companyPostPictureRepository.findById(id).orElseThrow(() -> new IdNotFoundException(NotFoundType.PICTURE));
        return new Picture(MediaType.valueOf(companyPostPicture.getContentType()), companyPostPicture.getContent());
    }

    private Company getCompany(Principal principal) throws IdNotFoundException {
        return companyRepository.findByName(principal.getName()).orElseThrow(IdNotFoundException.get(NotFoundType.COMPANY));
    }
}
