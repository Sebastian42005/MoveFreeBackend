package com.example.movefree.database.company.member.role;

import java.util.function.Function;

public class CompanyMemberRoleDTOMapper implements Function<CompanyMemberRole, CompanyMemberRoleDTO> {

    @Override
    public CompanyMemberRoleDTO apply(CompanyMemberRole companyMemberRole) {
        return new CompanyMemberRoleDTO(
                companyMemberRole.getId(),
                companyMemberRole.getName()
        );
    }
}
