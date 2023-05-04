package com.example.movefree.database.spot.spottype;

import java.util.function.Function;

public class SpotTypeDTOMapper implements Function<SpotType, SpotTypeDTO> {

    @Override
    public SpotTypeDTO apply(SpotType spotType) {
        return new SpotTypeDTO(
                spotType.getName()
        );
    }
}
