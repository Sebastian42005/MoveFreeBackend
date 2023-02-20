package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.role.CompanyMemberRole;
import com.example.movefree.database.company.picture.Picture;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
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
    private List<Picture> pictures;
}
