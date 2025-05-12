package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.sport.Sport;

public record MarkerDto(
        Integer spotId,
        String title,
        Double latitude,
        Double longitude,
        String city,
        Sport sport
) {
}
