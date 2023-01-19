package com.example.movefree.database.company.member.role;

import lombok.Data;

@Data
public class CompanyMemberRoleDTOResponse {
    int id;
    String name;

    public CompanyMemberRoleDTOResponse(CompanyMemberRoleDTO companyMemberRoleDTO) {
        this.id = companyMemberRoleDTO.getId();
        this.name = companyMemberRoleDTO.getName();
    }
}
