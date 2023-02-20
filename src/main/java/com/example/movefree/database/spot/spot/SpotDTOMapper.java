package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.image.SpotPictureDTO;

import java.util.function.Function;

public class SpotDTOMapper implements Function<Spot, SpotDTO> {
    @Override
    public SpotDTO apply(Spot spot) {
        return new SpotDTO(
                spot.getId(),
                spot.getDescription(),
                spot.getLocation(),
                spot.getSpotType(),
                spot.getUser().getUsername(),
                spot.getPictures().stream().map(SpotPictureDTO::getId).toList()
        );
    }
}
