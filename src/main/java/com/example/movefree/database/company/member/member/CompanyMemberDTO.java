package com.example.movefree.database.company.member.member;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;

import java.util.List;
import java.util.UUID;

public record CompanyMemberDTO(
        UUID id,
        String name,
        List<CompanyMemberRoleDTO> roles
){
}
