package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.spottype.AttributeDTO;

import java.util.List;

public record SpotDTO (
        Integer id,
        String title,
        String description,
        Location location,
        List<AttributeDTO> attributes,
        String sport,
        String user,
        String createdAt,
        Double rating,
        List<Integer> pictures
){
}
