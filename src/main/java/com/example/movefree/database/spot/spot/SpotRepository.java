package com.example.movefree.database.spot.spot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SpotRepository extends JpaRepository<SpotDTO, Integer> {

    @Query(value = "select * from spots s JOIN locations l on s.location_id = l.id WHERE lower(l.city) = in (:cities) AND s.spot_type in(:spotTypes) LIMIT :limit", nativeQuery = true)
    List<SpotDTO> searchWithFilter(List<String> cities, List<Integer> spotTypes, int limit);

    @Query(value = "select * from spots s WHERE s.spot_type in (:spotTypes) LIMIT :limit", nativeQuery = true)
    List<SpotDTO> searchWithSpotType(List<Integer> spotTypes, int limit);

    @Query(value = "select * from spots s JOIN locations l on s.location_id = l.id WHERE lower(l.city) = in (:cities) LIMIT :limit", nativeQuery = true)
    List<SpotDTO> searchWithCity(List<String> cities, int limit);

    @Query(value = "select * from spots LIMIT :limit", nativeQuery = true)
    List<SpotDTO> searchWithoutFilter(int limit);
}
