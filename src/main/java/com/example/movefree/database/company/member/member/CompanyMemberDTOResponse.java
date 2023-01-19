package com.example.movefree.database.company.member.member;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTOResponse;
import lombok.Data;

import java.util.List;

@Data
public class CompanyMemberDTOResponse {
    int id;
    String name;
    List<CompanyMemberRoleDTOResponse> roles;

    public CompanyMemberDTOResponse(CompanyMemberDTO companyMemberDTO) {
        this.id = companyMemberDTO.getId();
        this.name = companyMemberDTO.getName();
        this.roles = companyMemberDTO.getRoles().stream().map(CompanyMemberRoleDTOResponse::new).toList();
    }
}
