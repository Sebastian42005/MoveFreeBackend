package com.example.movefree.database.company.member.role;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.member.member.CompanyMemberDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "company_roles")
public class CompanyMemberRoleDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonBackReference("company_company_member_roles")
    @ManyToOne
    private CompanyDTO company;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "company_member_company_role",
            joinColumns = { @JoinColumn(name = "company_member_role_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "company_member_id", referencedColumnName = "id") })
    @JsonIgnore
    private List<CompanyMemberDTO> members = new ArrayList<>();
}
