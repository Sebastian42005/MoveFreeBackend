package com.example.movefree.port.spot;

import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;

import java.util.List;

public interface SpotPort {

    SpotDTO postSpot(PostSpotRequestBody spot, String name) throws IdNotFoundException;

    Rating rateSpot(int id, RateSpotRequestBody rating, String name) throws IdNotFoundException;

    List<Rating> getSpotRatings(int spotId) throws IdNotFoundException;

    List<SpotDTO> searchSpot(List<String> cities, List<String> spotTypes, int limit) throws InvalidInputException;
}
