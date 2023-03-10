package com.example.movefree.database.company.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRequestRepository extends JpaRepository<CompanyRequest, UUID> {

    Optional<CompanyRequest> findByUsername(String username);
}
