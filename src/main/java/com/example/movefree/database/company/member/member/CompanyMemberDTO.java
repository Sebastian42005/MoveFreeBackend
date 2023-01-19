package com.example.movefree.database.company.member.member;


import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.member.role.CompanyMemberRoleDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "company_members")
public class CompanyMemberDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private byte[] profilePicture;
    private String contentType;

    @JsonBackReference("company_company_member")
    @ManyToOne
    private CompanyDTO company;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompanyMemberRoleDTO> roles = new ArrayList<>();

}