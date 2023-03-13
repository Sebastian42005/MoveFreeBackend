package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTOMapper;

import java.util.List;
import java.util.function.Function;

public class CompanyDTOMapper implements Function<Company, CompanyDTO> {
    @Override
    public CompanyDTO apply(Company company) {
        if (company == null) return null;
        if (company.getMembers() == null) company.setMembers(List.of());
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getEmail(),
                company.getDescription(),
                company.getPhoneNumber(),
                company.getAddress(),
                company.getFollower().size(),
                company.getMembers().stream().map(new CompanyMemberDTOMapper()).toList()
        );
    }
}
