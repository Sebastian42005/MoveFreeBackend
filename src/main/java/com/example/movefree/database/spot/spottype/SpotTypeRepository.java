package com.example.movefree.database.spot.spottype;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotTypeRepository extends JpaRepository<SpotType, String> {

    Optional<SpotType> findByName(String name);
}
