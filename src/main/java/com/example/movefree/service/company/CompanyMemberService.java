package com.example.movefree.service.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.database.company.member.member.CompanyMemberDTOMapper;
import com.example.movefree.database.company.member.member.CompanyMemberRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.NoCompanyException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.exception.enums.enums.NotFoundType;
import com.example.movefree.file.ImageReader;
import com.example.movefree.port.company.CompanyMemberPort;
import com.example.portclass.Picture;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
public class CompanyMemberService implements CompanyMemberPort {

    final UserRepository userRepository;
    final CompanyMemberRepository memberRepository;

    final CompanyMemberDTOMapper companyMemberDTOMapper = new CompanyMemberDTOMapper();

    public CompanyMemberService(UserRepository userRepository, CompanyMemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public CompanyMemberDTO createMember(String member, Principal principal) throws UserForbiddenException {
        CompanyMember companyMember = new CompanyMember();
        companyMember.setName(member);
        User user = getUser(principal.getName());
        Company company = user.getCompany();
        companyMember.setCompany(company);
        CompanyMember savedMember = memberRepository.save(companyMember);
        company.getMembers().add(savedMember);
        return companyMemberDTOMapper.apply(savedMember);
    }

    @Override
    public Picture setProfilePicture(int id, MultipartFile image, Principal principal) throws NoCompanyException, UserForbiddenException, IdNotFoundException, IOException {
        checkForAuthorization(principal.getName(), id);
        CompanyMember companyMemberDTO = getCompanyMember(id);
        companyMemberDTO.setProfilePicture(image.getBytes());
        companyMemberDTO.setContentType(image.getContentType());
        memberRepository.save(companyMemberDTO);
        return new Picture(MediaType.valueOf(companyMemberDTO.getContentType()), companyMemberDTO.getProfilePicture());
    }

    @Override
    public Picture getProfilePicture(int id) throws IdNotFoundException {
        CompanyMember companyMember = getCompanyMember(id);
        if (companyMember.getProfilePicture() == null) return ImageReader.getProfilePicture();
        return new Picture(MediaType.valueOf(companyMember.getContentType()), companyMember.getProfilePicture());
    }

    private void checkForAuthorization(String username, int memberId) throws NoCompanyException, UserForbiddenException {
        User userDTO = getUser(username);
        if (userDTO.getCompany() == null) throw new NoCompanyException();
        if (userDTO.getCompany().getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new UserForbiddenException();
    }

    private User getUser(String username) throws UserForbiddenException {
        return userRepository.findByUsername(username).orElseThrow(UserForbiddenException::new);
    }

    private CompanyMember getCompanyMember(int id) throws IdNotFoundException {
        return memberRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }
}
