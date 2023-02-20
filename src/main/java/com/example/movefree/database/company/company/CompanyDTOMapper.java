package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTOMapper;

import java.util.function.Function;

public class CompanyDTOMapper implements Function<Company, CompanyDTO> {
    @Override
    public CompanyDTO apply(Company company) {
        if (company == null) return null;
        return new CompanyDTO(
                company.getId(),
                company.getPhoneNumber(),
                company.getAddress(),
                company.getMembers().stream().map(new CompanyMemberDTOMapper()).toList()
        );
    }
}
