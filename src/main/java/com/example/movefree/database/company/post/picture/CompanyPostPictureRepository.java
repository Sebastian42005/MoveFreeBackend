package com.example.movefree.database.company.post.picture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyPostPictureRepository extends JpaRepository<CompanyPostPicture, UUID> {
}
