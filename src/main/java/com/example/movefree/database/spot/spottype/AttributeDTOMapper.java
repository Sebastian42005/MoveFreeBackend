package com.example.movefree.database.spot.spottype;

import java.util.function.Function;

public class AttributeDTOMapper implements Function<Attribute, AttributeDTO> {

    @Override
    public AttributeDTO apply(Attribute attribute) {
        return new AttributeDTO(
                attribute.getId(),
                attribute.getName(),
                attribute.getColor()
        );
    }
}
