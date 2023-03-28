package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.image.SpotPicture;

import java.util.List;
import java.util.function.Function;

public class SpotDTOMapper implements Function<Spot, SpotDTO> {
    @Override
    public SpotDTO apply(Spot spot) {
        if (spot.getPictures() == null) spot.setPictures(List.of());
        return new SpotDTO(
                spot.getId(),
                spot.getDescription(),
                spot.getLocation(),
                spot.getSpotType(),
                spot.getUser().getUsername(),
                spot.getPictures().stream().map(SpotPicture::getId).toList()
        );
    }
}
