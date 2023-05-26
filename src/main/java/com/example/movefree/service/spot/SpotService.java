package com.example.movefree.service.spot;

import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.location.LocationRepository;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.rating.RatingMapper;
import com.example.movefree.database.spot.rating.RatingRepository;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOMapper;
import com.example.movefree.database.spot.spot.SpotRepository;
import com.example.movefree.database.spot.spottype.SpotType;
import com.example.movefree.database.spot.spottype.SpotTypeRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.port.spot.SpotPort;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpotService implements SpotPort {

    final SpotRepository spotRepository;
    final UserRepository userRepository;
    final LocationRepository locationRepository;
    final RatingRepository ratingRepository;
    final SpotTypeRepository spotTypeRepository;

    final RatingMapper ratingMapper = new RatingMapper();

    final SpotDTOMapper spotDTOMapper = new SpotDTOMapper();


    public SpotService(SpotRepository spotRepository, UserRepository userRepository, LocationRepository locationRepository, RatingRepository ratingRepository, SpotTypeRepository spotTypeRepository) {
        this.spotRepository = spotRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.ratingRepository = ratingRepository;
        this.spotTypeRepository = spotTypeRepository;
    }

    @Override
    public SpotDTO postSpot(PostSpotRequestBody postSpotRequestBody, String name) throws IdNotFoundException {
        //find User
        User user = userRepository.findByUsername(name).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
        List<SpotType> spotTypes = new ArrayList<>();
        postSpotRequestBody.getSpotTypes().forEach(spotType -> spotTypes.add(spotTypeRepository.findById(spotType).orElseThrow()));
        //create Location
        Location location = new Location();
        location.setLatitude(postSpotRequestBody.getLatitude());
        location.setLongitude(postSpotRequestBody.getLongitude());
        location.setCity(postSpotRequestBody.getCity());
        //create Spot
        Spot spot = new Spot();
        spot.setRatings(new ArrayList<>());
        spot.setDescription(postSpotRequestBody.getDescription());
        spot.setUser(user);
        spot.setLocation(locationRepository.save(location));
        spot.setSpotTypes(spotTypes);
        spot.setCreatedAt(Instant.now());
        return spotDTOMapper.apply(spotRepository.save(spot));
    }

    @Override
    public RatingDTO rateSpot(UUID id, RateSpotRequestBody rateSpotRequestBody, String name) throws IdNotFoundException {
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
        return ratingMapper.apply(savedRating);
    }

    @Override
    public List<RatingDTO> getSpotRatings(UUID spotId) throws IdNotFoundException {
        Spot spot = spotRepository.findById(spotId).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        return spot.getRatings().stream().map(ratingMapper).toList();
    }

    @Override
    public Map<String, Object> searchSpot(String search, String spotType, int limit, List<UUID> alreadySeenList) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        if (alreadySeenList.isEmpty()) alreadySeenList = List.of(UUID.randomUUID());
        List<Spot> spotList;
        if (spotType.equals("")) {
            spotList = spotRepository.searchSpotNoSpotType(search, alreadySeenList, pageable);
        }else {
            spotList = spotRepository.searchSpot(search, spotType, alreadySeenList, pageable);
        }
        Map<String, Object> map = new HashMap<>();
        Boolean hasMore = spotList.size() > limit;
        if (Boolean.TRUE.equals(hasMore)) spotList.remove(spotList.size() - 1);
        map.put("spots", spotList.stream().map(spotDTOMapper).toList());
        map.put("hasMore", hasMore);
        return map;
    }

    @Override
    public SpotDTO getSpot(UUID id) throws IdNotFoundException {
        return spotDTOMapper.apply(spotRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT)));
    }
}
