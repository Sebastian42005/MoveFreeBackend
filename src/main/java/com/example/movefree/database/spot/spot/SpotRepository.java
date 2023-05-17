package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.spottype.SpotType;
import com.example.movefree.database.spot.spottype.SpotTypeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpotRepository extends JpaRepository<Spot, UUID> {

    @Query("SELECT spot FROM Spot spot " +
            "JOIN spot.spotTypes spotType " +
            "WHERE (lower(spot.location.city) LIKE lower(concat('%', :search,'%')) " +
            "OR lower(spot.description) LIKE lower(concat('%', :search,'%'))) " +
            "AND lower(spotType.name) = lower(:spotType) " +
            "AND spot.id NOT IN (:alreadySeenList)")
    List<Spot> searchSpot(String search, String spotType, List<UUID> alreadySeenList, Pageable pageable);
    @Query("SELECT spot FROM Spot spot WHERE (lower(spot.location.city) LIKE lower(concat('%', :search,'%')) " +
            "OR lower(spot.description) LIKE lower(concat('%', :search,'%'))) " +
            "AND spot.id NOT IN(:alreadySeenList)")
    List<Spot> searchSpotNoSpotType(String search, List<UUID> alreadySeenList, Pageable pageable);

}
