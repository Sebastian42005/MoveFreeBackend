package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.Location;

import java.util.List;

public record SpotDTO (
        Integer id,
        String description,
        Location location,
        List<String> spotTypes,
        String user,
        String createdAt,
        Double rating,
        List<Integer> pictures
){
}
