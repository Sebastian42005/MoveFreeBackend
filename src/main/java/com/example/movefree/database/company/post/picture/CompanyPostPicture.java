package com.example.movefree.database.company.post.picture;

import com.example.movefree.database.company.post.CompanyPost;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_post_pictures")
public class CompanyPostPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    byte[] content;

    @Column(nullable = false)
    String contentType;

    @JsonBackReference("post_pictures")
    @ManyToOne
    private CompanyPost post;

}
