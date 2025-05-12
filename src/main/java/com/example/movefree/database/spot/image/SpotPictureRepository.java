package com.example.movefree.database.spot.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SpotPictureRepository extends JpaRepository<SpotPicture, Integer> {

    @Query("SELECT new com.example.movefree.database.spot.image.LowResPicture(spotPicture.lowResPicture, spotPicture.contentType) FROM SpotPicture spotPicture WHERE spotPicture.id = :id")
    LowResPicture getLowResPicture(Integer id);
}
