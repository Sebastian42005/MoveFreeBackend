package com.example.movefree.service.company;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.example.movefree.database.company.member.member.CompanyMemberDTOMapper;
import com.example.movefree.database.company.member.member.CompanyMemberRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.file.ImageReader;
import com.example.movefree.port.company.CompanyMemberPort;
import com.example.portclass.Picture;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@Service
public class CompanyMemberService implements CompanyMemberPort {

    final CompanyRepository companyRepository;
    final CompanyMemberRepository memberRepository;

    final CompanyMemberDTOMapper companyMemberDTOMapper = new CompanyMemberDTOMapper();

    public CompanyMemberService(CompanyRepository companyRepository, CompanyMemberRepository memberRepository) {
        this.companyRepository = companyRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public CompanyMemberDTO createMember(String member, Principal principal) throws IdNotFoundException {
        CompanyMember companyMember = new CompanyMember();
        companyMember.setName(member);
        Company company = getCompany(principal.getName());
        companyMember.setCompany(company);
        CompanyMember savedMember = memberRepository.save(companyMember);
        company.getMembers().add(savedMember);
        return companyMemberDTOMapper.apply(savedMember);
    }

    @Override
    public Picture setProfilePicture(UUID id, MultipartFile image, Principal principal) throws UserForbiddenException, IdNotFoundException, IOException {
        checkForAuthorization(principal.getName(), id);
        CompanyMember companyMemberDTO = getCompanyMember(id);
        companyMemberDTO.setProfilePicture(image.getBytes());
        companyMemberDTO.setContentType(image.getContentType());
        memberRepository.save(companyMemberDTO);
        return new Picture(MediaType.valueOf(companyMemberDTO.getContentType()), companyMemberDTO.getProfilePicture());
    }

    @Override
    public Picture getProfilePicture(UUID id) throws IdNotFoundException {
        CompanyMember companyMember = getCompanyMember(id);
        if (companyMember.getProfilePicture() == null) return ImageReader.getProfilePicture();
        return new Picture(MediaType.valueOf(companyMember.getContentType()), companyMember.getProfilePicture());
    }

    private void checkForAuthorization(String username, UUID memberId) throws UserForbiddenException, IdNotFoundException {
        Company company = getCompany(username);
        if (company.getMembers().stream().noneMatch(member -> member.getId() == memberId)) throw new UserForbiddenException();
    }

    private Company getCompany(String name) throws IdNotFoundException {
        return companyRepository.findByName(name).orElseThrow(IdNotFoundException.get(NotFoundType.COMPANY));
    }

    private CompanyMember getCompanyMember(UUID id) throws IdNotFoundException {
        return memberRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.MEMBER));
    }
}
