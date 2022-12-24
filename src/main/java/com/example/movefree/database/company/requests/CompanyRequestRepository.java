package com.example.movefree.database.company.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRequestRepository extends JpaRepository<CompanyRequestDTO, Integer> {

    Optional<CompanyRequestDTO> findByUsername(String username);
}
