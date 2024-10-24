package com.example.movefree.database.company.company;

import com.example.movefree.database.company.member.member.CompanyMember;
import com.example.movefree.database.company.member.role.CompanyMemberRole;
import com.example.movefree.database.company.post.CompanyPost;
import com.example.movefree.database.timetable.course.Course;
import com.example.movefree.database.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "company_follower",
            joinColumns = { @JoinColumn(name = "company_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
    @JsonIgnore
    private List<User> follower = new ArrayList<>();

    private String name;
    private String email;
    private String password;
    private String description;
    private String phoneNumber;
    private String address;

    private byte[] profilePicture;
    private String contentType;

    @JsonManagedReference("company_timetable")
    @OneToMany(mappedBy = "company")
    private List<Course> timetable;

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
