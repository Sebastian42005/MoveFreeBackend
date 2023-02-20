package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;

import java.util.List;

public record CompanyDTO(
        int id,
        String phoneNumber,
        String address,
        List<CompanyMemberDTO> members
) {
}
