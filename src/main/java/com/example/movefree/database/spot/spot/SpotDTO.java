package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.Location;

import java.util.List;
import java.util.UUID;

public record SpotDTO (
        UUID id,
        String description,
        Location location,
        List<String> spotTypes,
        String user,
        String createdAt,
        Double rating,
        List<UUID> pictures
){
}
