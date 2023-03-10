package com.example.movefree.database.company.post;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.post.picture.CompanyPostPicture;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_posts")
public class CompanyPost {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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
