package com.example.movefree.database.company.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyPostRepository extends JpaRepository<CompanyPost, UUID> {
}
