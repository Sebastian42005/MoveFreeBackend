package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.spottype.SpotType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SpotDTO (
        UUID id,
        String description,
        Location location,
        List<SpotType> spotTypes,
        String user,
        Instant createdAt,
        Double rating,
        List<UUID> pictures
){
}
