package com.example.movefree.database.company.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRequestRepository extends JpaRepository<CompanyRequest, Integer> {

    Optional<CompanyRequest> findByUsername(String username);
}
