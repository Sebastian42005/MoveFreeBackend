package com.example.movefree.database.company.post;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.post.picture.CompanyPostPicture;
import com.example.movefree.database.spot.spot.Spot;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_posts")
public class CompanyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
