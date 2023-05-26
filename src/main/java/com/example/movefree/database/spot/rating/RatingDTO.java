package com.example.movefree.database.spot.rating;

import java.util.UUID;

public record RatingDTO(
        UUID id,
        String message,
        String username,
        int stars
) {
}
