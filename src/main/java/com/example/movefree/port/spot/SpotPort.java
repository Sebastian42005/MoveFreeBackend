package com.example.movefree.port.spot;

import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface SpotPort {

    SpotDTO postSpot(PostSpotRequestBody spot, String name) throws IdNotFoundException;

    RatingDTO rateSpot(Integer id, RateSpotRequestBody rating, String name) throws IdNotFoundException;

    List<RatingDTO> getSpotRatings(Integer spotId) throws IdNotFoundException;

    Map<String, Object> searchSpot(String search, String spotType, int limit, List<Integer> alreadySeenList) throws IdNotFoundException;

    SpotDTO getSpot(Integer id) throws IdNotFoundException;

    void deleteSpot(Integer id, Principal principal) throws IdNotFoundException, UserForbiddenException;

    void saveSpot(Integer id, Principal principal) throws IdNotFoundException;

    Map<String, Object> getSavedSpots(Principal principal, List<Integer> alreadySeenList, int limit);

    boolean isSaved(Integer id, Principal principal);
}
