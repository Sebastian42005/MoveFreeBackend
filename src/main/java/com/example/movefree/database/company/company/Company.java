package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.role.CompanyMemberRole;
import com.example.movefree.database.company.post.CompanyPost;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String phoneNumber;
    private String address;

    @JsonManagedReference("company_company_member")
    @OneToMany(mappedBy = "company")
    private List<CompanyMember> members;

    @JsonManagedReference("company_company_member_roles")
    @OneToMany(mappedBy = "company")
    private List<CompanyMemberRole> memberRoles;

    @JsonManagedReference("company_pictures")
    @OneToMany(mappedBy = "company")
    private List<CompanyPost> companyPosts;
}
