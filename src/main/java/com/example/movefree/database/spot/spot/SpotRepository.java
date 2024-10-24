package com.example.movefree.database.spot.spot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Integer> {

    @Query("SELECT spot FROM Spot spot " +
            "JOIN spot.spotTypes spotType " +
            "WHERE (lower(spot.location.city) LIKE lower(concat('%', :search,'%')) " +
            "OR lower(spot.description) LIKE lower(concat('%', :search,'%'))) " +
            "AND lower(spotType.name) = lower(:spotType) " +
            "AND spot.id NOT IN (:alreadySeenList) ORDER BY function('RAND')")
    List<Spot> searchSpot(String search, String spotType, List<Integer> alreadySeenList, Pageable pageable);

    @Query("SELECT spot FROM Spot spot WHERE (lower(spot.location.city) LIKE lower(concat('%', :search,'%')) " +
            "OR lower(spot.description) LIKE lower(concat('%', :search,'%'))) " +
            "AND spot.id NOT IN(:alreadySeenList) ORDER BY function('RAND')")
    List<Spot> searchSpotNoSpotType(String search, List<Integer> alreadySeenList, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(savedByUsers) > 0 THEN true ELSE false END FROM Spot spot JOIN spot.savedBy savedByUsers WHERE spot.id = :spotId AND :username = savedByUsers.username")
    boolean isSaved(Integer spotId, String username);
}
