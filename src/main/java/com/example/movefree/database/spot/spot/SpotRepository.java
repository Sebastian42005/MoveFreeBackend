package com.example.movefree.database.spot.spot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Integer> {

    @Query("select spot from Spot spot WHERE lower(spot.location.city) in :cities AND spot.spotType in (:spotTypes)")
    List<Spot> searchWithFilter(List<String> cities, List<Integer> spotTypes);

    @Query("select spot from Spot spot WHERE spot.spotType in (:spotTypes)")
    List<Spot> searchWithSpotType(List<Integer> spotTypes, int limit);

    @Query("select spot from Spot spot WHERE lower(spot.location.city) = in (:cities)")
    List<Spot> searchWithCity(List<String> cities, int limit);

    @Query("select spot from Spot spot")
    List<Spot> searchWithoutFilter(int limit);
}
