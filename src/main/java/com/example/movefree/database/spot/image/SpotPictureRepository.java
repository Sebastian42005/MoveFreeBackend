package com.example.movefree.database.spot.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpotPictureRepository extends JpaRepository<SpotPicture, UUID> {
}
