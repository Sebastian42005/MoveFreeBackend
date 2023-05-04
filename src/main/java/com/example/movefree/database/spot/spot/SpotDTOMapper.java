package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.image.SpotPicture;
import com.example.movefree.database.spot.rating.Rating;

import java.util.List;
import java.util.function.Function;

public class SpotDTOMapper implements Function<Spot, SpotDTO> {
    @Override
    public SpotDTO apply(Spot spot) {
        if (spot.getPictures() == null) spot.setPictures(List.of());
        double rating = spot.getRatings().stream().mapToDouble(Rating::getStars).average().orElse(0);
        rating = Math.round(rating * 100.0) / 100.0;
        return new SpotDTO(
                spot.getId(),
                spot.getDescription(),
                spot.getLocation(),
                spot.getSpotTypes(),
                spot.getUser().getUsername(),
                spot.getCreatedAt(),
                rating,
                spot.getPictures().stream().map(SpotPicture::getId).toList()
        );
    }
}
