package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.spottype.SpotType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpotRepository extends JpaRepository<Spot, UUID> {

    @Query("select spot from Spot spot WHERE lower(spot.location.city) in :cities AND spot.spotType in (:spotTypes) AND spot.id NOT IN(:alreadySeenList)")
    List<Spot> findSpotByFilter(List<String> cities, List<Integer> spotTypes, List<UUID> alreadySeenList, Pageable pageable);

    @Query("SELECT spot FROM Spot spot WHERE spot.spotType IN (:spotTypes) AND spot.id NOT IN(:alreadySeenList)")
    List<Spot> findSpotBySpotTypes(List<SpotType> spotTypes, List<UUID> alreadySeenList, Pageable pageable);


    @Query("select spot from Spot spot WHERE lower(spot.location.city) in (:cities) AND spot.id NOT IN(:alreadySeenList)")
    List<Spot> findSpotByCities(List<String> cities, List<UUID> alreadySeenList, Pageable pageable);

    @Query("select spot from Spot spot WHERE spot.id NOT IN(:alreadySeenList)")
    List<Spot> searchWithoutFilter(List<UUID> alreadySeenList, Pageable pageable);
}
