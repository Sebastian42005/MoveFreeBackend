package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.spottype.SpotType;

import java.util.List;
import java.util.UUID;

public record SpotDTO (
        UUID id,
        String description,
        Location location,
        SpotType spotType,
        String user,
        List<UUID> pictures
){
}
