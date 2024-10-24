package com.example.movefree.database.company.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE c.name = :username AND c.password = :password")
    Optional<Company> login(String username, String password);
}
