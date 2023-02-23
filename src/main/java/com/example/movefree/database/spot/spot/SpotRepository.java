package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.spottype.SpotType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Integer> {

    @Query("select spot from Spot spot WHERE lower(spot.location.city) in :cities AND spot.spotType in (:spotTypes)")
    List<Spot> findSpotByFilter(List<String> cities, List<Integer> spotTypes);

    @Query("SELECT spot FROM Spot spot WHERE spot.spotType IN (:spotTypes)")
    List<Spot> findSpotBySpotTypes(List<SpotType> spotTypes, Pageable pageable);


    @Query("select spot from Spot spot WHERE lower(spot.location.city) in (:cities)")
    List<Spot> findSpotByCities(List<String> cities, Pageable pageable);

    @Query("select spot from Spot spot")
    List<Spot> searchWithoutFilter(Pageable pageable);
}
