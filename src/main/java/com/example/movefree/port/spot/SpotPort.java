package com.example.movefree.port.spot;

import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;

import java.util.List;
import java.util.UUID;

public interface SpotPort {

    SpotDTO postSpot(PostSpotRequestBody spot, String name) throws IdNotFoundException;

    Rating rateSpot(UUID id, RateSpotRequestBody rating, String name) throws IdNotFoundException;

    List<Rating> getSpotRatings(UUID spotId) throws IdNotFoundException;

    List<SpotDTO> searchSpot(String search, String spotType, int limit, List<UUID> alreadySeenList);

}
