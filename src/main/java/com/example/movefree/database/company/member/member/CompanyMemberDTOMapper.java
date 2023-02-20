package com.example.movefree.database.company.member.member;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTOMapper;

import java.util.function.Function;

public class CompanyMemberDTOMapper implements Function<CompanyMember, CompanyMemberDTO> {

    @Override
    public CompanyMemberDTO apply(CompanyMember companyMember) {
        return new CompanyMemberDTO(
                companyMember.getId(),
                companyMember.getName(),
                companyMember.getRoles().stream().map(new CompanyMemberRoleDTOMapper()).toList()
                );
    }
}
