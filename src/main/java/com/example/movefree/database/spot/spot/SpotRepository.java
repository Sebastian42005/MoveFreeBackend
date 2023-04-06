package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.spottype.SpotType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpotRepository extends JpaRepository<Spot, UUID> {

    @Query("SELECT spot FROM Spot spot WHERE (lower(spot.location.city) LIKE lower(concat('%', :search,'%')) OR lower(spot.description) LIKE  lower(concat('%', :search,'%'))) AND spot.spotType IN (:spotTypes) AND spot.id NOT IN(:alreadySeenList)")
    List<Spot> searchSpot(String search, List<SpotType> spotTypes, List<UUID> alreadySeenList, Pageable pageable);

    @Query("SELECT spot FROM Spot spot WHERE (lower(spot.location.city) LIKE lower(concat('%', :search,'%')) OR lower(spot.description) LIKE  lower(concat('%', :search,'%'))) AND spot.id NOT IN(:alreadySeenList)")
    List<Spot> searchSpotNoSpotType(String search, List<UUID> alreadySeenList, Pageable pageable);
}
