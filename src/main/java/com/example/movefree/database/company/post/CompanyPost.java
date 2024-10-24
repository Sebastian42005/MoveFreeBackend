package com.example.movefree.database.company.post;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.post.picture.CompanyPostPicture;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_posts")
public class CompanyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private byte[] content;

    private String contentType;

    private String description;

    @JsonManagedReference("post_pictures")
    @OneToMany(mappedBy = "post")
    private List<CompanyPostPicture> pictures;

    @JsonBackReference("company_pictures")
    @ManyToOne
    private Company company;
}
