package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.sport.Sport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Integer> {

    @Query("SELECT CASE WHEN COUNT(savedByUsers) > 0 THEN true ELSE false END FROM Spot spot JOIN spot.savedBy savedByUsers WHERE spot.id = :spotId AND :username = savedByUsers.username")
    boolean isSaved(Integer spotId, String username);

    @Query("SELECT DISTINCT new com.example.movefree.database.spot.spot.MarkerDto(" +
            "spot.id, spot.title, spot.location.latitude, spot.location.longitude, spot.location.city, spot.sport) " +
            "FROM Spot spot " +
            "LEFT JOIN spot.ratings r " +
            "LEFT JOIN spot.attributes a " +
            "WHERE (:minRating IS NULL OR (SELECT AVG(r2.stars) FROM Rating r2 WHERE r2.spot = spot) >= :minRating) " +
            "AND (:sport IS NULL OR spot.sport.name = :sport) " +
            "AND (:search IS NULL OR LOWER(spot.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:city IS NULL OR LOWER(spot.location.city) = LOWER(:city)) " +
            "AND a.id IN :attributeIds")
    List<MarkerDto> getMarkersWithAttributes(@Param("minRating") Double minRating,
                                             @Param("sport") String sport,
                                             @Param("search") String search,
                                             @Param("city") String city,
                                             @Param("attributeIds") List<Long> attributeIds);


    @Query("SELECT DISTINCT new com.example.movefree.database.spot.spot.MarkerDto(" +
            "spot.id, spot.title, spot.location.latitude, spot.location.longitude, spot.location.city, spot.sport) " +
            "FROM Spot spot " +
            "LEFT JOIN spot.ratings r " +
            "WHERE (:minRating IS NULL OR (SELECT AVG(r2.stars) FROM Rating r2 WHERE r2.spot = spot) >= :minRating) " +
            "AND (:sport IS NULL OR spot.sport.name = :sport) " +
            "AND (:search IS NULL OR LOWER(spot.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:city IS NULL OR LOWER(spot.location.city) = LOWER(:city))")
    List<MarkerDto> getMarkersWithoutAttributes(@Param("minRating") Double minRating,
                                                @Param("sport") String sport,
                                                @Param("search") String search,
                                                @Param("city") String city);

    @Query("SELECT spot " +
            "FROM Spot spot " +
            "LEFT JOIN spot.ratings r " +
            "LEFT JOIN spot.attributes a " +
            "WHERE (:minRating IS NULL OR (SELECT AVG(r2.stars) FROM Rating r2 WHERE r2.spot = spot) >= :minRating) " +
            "AND (:sport IS NULL OR spot.sport.name = :sport) " +
            "AND (:search IS NULL OR LOWER(spot.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:city IS NULL OR LOWER(spot.location.city) = LOWER(:city)) " +
            "AND a.id IN :attributeIds " +
            "AND spot.id NOT IN(:alreadySeenList) " +
            "GROUP BY spot.id ORDER BY function('RAND')")
    List<Spot> getSpotsWithAttributes(@Param("minRating") Double minRating,
                                              @Param("sport") String sport,
                                              @Param("search") String search,
                                              @Param("city") String city,
                                              @Param("attributeIds") List<Long> attributeIds,
                                              @Param("alreadySeenList") List<Integer> alreadySeenList,
                                              Pageable pageable);


    @Query("SELECT spot " +
            "FROM Spot spot " +
            "LEFT JOIN spot.ratings r " +
            "WHERE (:minRating IS NULL OR (SELECT AVG(r2.stars) FROM Rating r2 WHERE r2.spot = spot) >= :minRating) " +
            "AND (:sport IS NULL OR spot.sport.name = :sport) " +
            "AND (:search IS NULL OR LOWER(spot.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:city IS NULL OR LOWER(spot.location.city) = LOWER(:city)) " +
            "AND spot.id NOT IN(:alreadySeenList) " +
            "GROUP BY spot.id ORDER BY function('RAND')")
    List<Spot> getSpotsWithoutAttributes(@Param("minRating") Double minRating,
                                              @Param("sport") String sport,
                                              @Param("search") String search,
                                              @Param("city") String city,
                                              @Param("alreadySeenList") List<Integer> alreadySeenList,
                                              Pageable pageable);

    List<Spot> findTopBySportInOrderByRatingsDesc(List<Sport> sports, Pageable pageable);
}