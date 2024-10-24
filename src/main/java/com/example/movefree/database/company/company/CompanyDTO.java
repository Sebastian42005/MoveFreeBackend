package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;

import java.util.List;

public record CompanyDTO(
        Integer id,
        String name,
        String email,
        String description,
        String phoneNumber,
        String address,
        int followers,
        List<CompanyMemberDTO> members
) {
}
