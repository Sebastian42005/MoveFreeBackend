package com.example.movefree.service.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyDTOMapper;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.post.CompanyPost;
import com.example.movefree.database.company.post.CompanyPostRepository;
import com.example.movefree.database.company.post.picture.CompanyPostPicture;
import com.example.movefree.database.company.post.picture.CompanyPostPictureRepository;
import com.example.movefree.database.company.requests.CompanyRequest;
import com.example.movefree.database.company.requests.CompanyRequestRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.AlreadyCompanyException;
import com.example.movefree.exception.AlreadySentRequestException;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.exception.NoCompanyException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.exception.enums.MultipartFileExceptionType;
import com.example.movefree.exception.enums.enums.NotFoundType;
import com.example.movefree.port.company.CompanyPort;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.role.Role;
import com.example.portclass.Picture;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
public class CompanyService implements CompanyPort {

    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final CompanyRequestRepository companyRequestRepository;
    final CompanyPostPictureRepository companyPostPictureRepository;
    final CompanyPostRepository companyPostRepository;

    final CompanyDTOMapper companyDTOMapper = new CompanyDTOMapper();

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, CompanyRequestRepository companyRequestRepository, CompanyPostPictureRepository companyPostPictureRepository, CompanyPostRepository companyPostRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyRequestRepository = companyRequestRepository;
        this.companyPostPictureRepository = companyPostPictureRepository;
        this.companyPostRepository = companyPostRepository;
    }

    @Override
    public CompanyDTO editCompany(PostSpotRequestBody.CompanyEditRequestBody company, Principal principal) throws UserForbiddenException, NoCompanyException {
        String phoneNumber = company.getPhoneNumber();
        String address = company.getAddress();
        Company userCompany = getCompany(principal);
        if (address != null) userCompany.setAddress(address);
        if (phoneNumber != null) userCompany.setPhoneNumber(phoneNumber);
        return companyDTOMapper.apply(companyRepository.save(userCompany));
    }

    @Override
    public CompanyRequest requestCompany(CompanyRequest.Request message, Principal principal) throws UserForbiddenException, AlreadyCompanyException, AlreadySentRequestException {
        CompanyRequest companyRequest = new CompanyRequest();
        //get user
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(UserForbiddenException::new);
        //check if user already is company
        if (user.getRole().equals(Role.COMPANY))
            throw new AlreadyCompanyException();
        //check if user already sent request
        if (companyRequestRepository.findByUsername(principal.getName()).isPresent())
            throw new AlreadySentRequestException();
        //send request
        companyRequest.setUsername(principal.getName());
        companyRequest.setMessage(message.getMessage());
        return companyRequestRepository.save(companyRequest);
    }

    @Override
    public void uploadPost(Principal principal, List<MultipartFile> pictures, String description) throws UserForbiddenException, InvalidMultipartFileException, NoCompanyException {
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
    public Picture getPicture(int id) throws IdNotFoundException {
        CompanyPostPicture companyPostPicture = companyPostPictureRepository.findById(id).orElseThrow(() -> new IdNotFoundException(NotFoundType.PICTURE));
        return new Picture(MediaType.valueOf(companyPostPicture.getContentType()), companyPostPicture.getContent());
    }

    private Company getCompany(Principal principal) throws UserForbiddenException, NoCompanyException {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(UserForbiddenException::new);
        Company company = user.getCompany();
        if (company == null) throw new NoCompanyException();
        return company;
    }
}
