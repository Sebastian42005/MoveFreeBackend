package com.example.movefree.database.company.post.picture;

import com.example.movefree.database.company.post.CompanyPost;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company_post_pictures")
public class CompanyPostPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    byte[] content;

    @Column(nullable = false)
    String contentType;

    @JsonBackReference("post_pictures")
    @ManyToOne
    private CompanyPost post;

}
