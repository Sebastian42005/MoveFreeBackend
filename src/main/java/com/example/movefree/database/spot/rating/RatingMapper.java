package com.example.movefree.database.spot.rating;

import java.util.function.Function;

public class RatingMapper implements Function<Rating, RatingDTO> {

    @Override
    public RatingDTO apply(Rating rating) {
        return new RatingDTO(rating.getId(),
                rating.getMessage(),
                rating.getUser().getUsername(),
                rating.getStars());
    }
}
