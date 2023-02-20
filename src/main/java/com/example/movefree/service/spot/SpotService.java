package com.example.movefree.service.spot;

import com.example.movefree.database.spot.location.LocationDTO;
import com.example.movefree.database.spot.location.LocationRepository;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.rating.RatingRepository;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOMapper;
import com.example.movefree.database.spot.spot.SpotRepository;
import com.example.movefree.database.spot.spotType.SpotType;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.enums.enums.NotFoundType;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.port.spot.SpotPort;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpotService implements SpotPort {


    final SpotRepository spotRepository;
    final UserRepository userRepository;
    final LocationRepository locationRepository;
    final RatingRepository ratingRepository;

    final SpotDTOMapper spotDTOMapper = new SpotDTOMapper();


    public SpotService(SpotRepository spotRepository, UserRepository userRepository, LocationRepository locationRepository, RatingRepository ratingRepository) {
        this.spotRepository = spotRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public SpotDTO postSpot(PostSpotRequestBody postSpotRequestBody, String name) throws IdNotFoundException {
        //find User
        User user = userRepository.findByUsername(name).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
        //create Location
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(postSpotRequestBody.getLatitude());
        locationDTO.setLongitude(postSpotRequestBody.getLongitude());
        locationDTO.setCity(postSpotRequestBody.getCity());
        //create Spot
        Spot spot = new Spot();
        spot.setRatings(new ArrayList<>());
        spot.setDescription(postSpotRequestBody.getDescription());
        spot.setUser(user);
        spot.setLocation(locationRepository.save(locationDTO));
        spot.setSpotType(postSpotRequestBody.getSpotType());
        return spotDTOMapper.apply(spotRepository.save(spot));
    }

    @Override
    public Rating rateSpot(int id, RateSpotRequestBody rateSpotRequestBody, String name) throws IdNotFoundException {
        //Get User and Spot
        User userDTO = userRepository.findByUsername(name).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
        Spot spotDTO = spotRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        Optional<Rating> userRating = spotDTO.getRatings().stream().filter(rating -> rating.getUser().getUsername().equals(name)).findFirst();
        //create Rating
        Rating rating = new Rating(rateSpotRequestBody.getMessage(), userDTO, spotDTO, rateSpotRequestBody.getStars());
        userRating.ifPresent(dto -> rating.setId(dto.getId()));
        //save Rating to Database
        Rating savedRating = ratingRepository.save(rating);
        spotDTO.getRatings().add(savedRating);
        spotRepository.save(spotDTO);
        return savedRating;
    }

    @Override
    public List<Rating> getSpotRatings(int spotId) throws IdNotFoundException {
        Spot spot = spotRepository.findById(spotId).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        return spot.getRatings();
    }

    @Override
    public List<SpotDTO> searchSpot(List<String> cities, List<SpotType> spotTypes, int limit) {
        List<Spot> spotDTOList;
        if (cities.isEmpty()) {
            if (spotTypes.isEmpty()) spotDTOList = spotRepository.searchWithoutFilter(limit);
            else spotDTOList = spotRepository.searchWithSpotType(spotTypes.stream().map(Enum::ordinal).toList(), limit);
        }else {
            if (spotTypes.isEmpty()) spotDTOList = spotRepository.searchWithCity(cities.stream().map(String::toLowerCase).toList(), limit);

            else spotDTOList = spotRepository.searchWithFilter(cities.stream().map(String::toLowerCase).toList(), spotTypes.stream().map(Enum::ordinal).toList());
        }
        return spotDTOList.stream().map(spotDTOMapper).toList();
    }
}
