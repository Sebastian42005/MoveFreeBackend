package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTO;

import java.util.List;
import java.util.UUID;

public record CompanyDTO(
        UUID id,
        String name,
        String email,
        String description,
        String phoneNumber,
        String address,
        int followers,
        List<CompanyMemberDTO> members
) {
}
