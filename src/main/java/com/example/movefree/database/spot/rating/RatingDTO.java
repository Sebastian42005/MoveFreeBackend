package com.example.movefree.database.spot.rating;

public record RatingDTO(
        Integer id,
        String message,
        String username,
        int stars
) {
}
