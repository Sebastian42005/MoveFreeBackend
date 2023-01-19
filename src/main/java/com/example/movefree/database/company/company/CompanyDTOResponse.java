package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMemberDTOResponse;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDTOResponse {
    private int id;
    private String phoneNumber;
    private String address;
    private List<CompanyMemberDTOResponse> members;

    public CompanyDTOResponse(CompanyDTO companyDTO) {
        this.id = companyDTO.getId();
        this.phoneNumber = companyDTO.getPhoneNumber();
        this.address = companyDTO.getAddress();
        this.members = companyDTO.getMembers().stream().map(CompanyMemberDTOResponse::new).toList();
    }
}
