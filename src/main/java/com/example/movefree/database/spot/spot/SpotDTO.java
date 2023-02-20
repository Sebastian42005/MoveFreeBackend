package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.LocationDTO;
import com.example.movefree.database.spot.spotType.SpotType;

import java.util.List;

public record SpotDTO (
        int id,
        String description,
        LocationDTO locationDTO,
        SpotType spotType,
        String user,
        List<Integer> pictures
){
}
