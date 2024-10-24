package com.example.movefree.database.company.member.member;


import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.member.role.CompanyMemberRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_members")
public class CompanyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private byte[] profilePicture;
    private String contentType;

    @JsonBackReference("company_company_member")
    @ManyToOne
    private Company company;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompanyMemberRole> roles = new ArrayList<>();

}
