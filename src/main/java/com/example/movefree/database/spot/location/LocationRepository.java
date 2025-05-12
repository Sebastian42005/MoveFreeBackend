package com.example.movefree.database.spot.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("SELECT DISTINCT city FROM Location WHERE city IS NOT NULL AND city <> ''")
    List<String> getAllCities();
}
