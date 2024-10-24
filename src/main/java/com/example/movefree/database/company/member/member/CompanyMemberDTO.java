package com.example.movefree.database.company.member.member;

import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;

import java.util.List;

public record CompanyMemberDTO(
        Integer id,
        String name,
        List<CompanyMemberRoleDTO> roles
){
}
