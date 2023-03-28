package com.example.movefree.service.spot;

import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.location.LocationRepository;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.rating.RatingRepository;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOMapper;
import com.example.movefree.database.spot.spot.SpotRepository;
import com.example.movefree.database.spot.spottype.SpotType;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.port.spot.SpotPort;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        spot.setSpotType(postSpotRequestBody.getSpotType());
        return spotDTOMapper.apply(spotRepository.save(spot));
    }

    @Override
    public Rating rateSpot(UUID id, RateSpotRequestBody rateSpotRequestBody, String name) throws IdNotFoundException {
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
    public List<Rating> getSpotRatings(UUID spotId) throws IdNotFoundException {
        Spot spot = spotRepository.findById(spotId).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        return spot.getRatings();
    }

    @Override
    public List<SpotDTO> searchSpot(List<String> cities, List<String> spotTypesAsString, int limit, List<UUID> alreadySeenList) throws InvalidInputException {
        List<SpotType> spotTypes = getSpotTypeList(spotTypesAsString);
        Pageable pageable = PageRequest.of(0, limit);
        if (alreadySeenList.isEmpty()) alreadySeenList = List.of(UUID.randomUUID());
        List<Spot> spotDTOList;
        if (cities.isEmpty()) {
            if (spotTypes.isEmpty()) spotDTOList = spotRepository.searchWithoutFilter(alreadySeenList, pageable);
            else spotDTOList = spotRepository.findSpotBySpotTypes(spotTypes, alreadySeenList,  pageable);
        }else {
            if (spotTypes.isEmpty()) spotDTOList = spotRepository.findSpotByCities(cities.stream().map(String::toLowerCase).toList(), alreadySeenList, pageable);

            else spotDTOList = spotRepository.findSpotByFilter(cities.stream().map(String::toLowerCase).toList(),
                    spotTypes.stream().map(Enum::ordinal).toList(),
                    alreadySeenList,
                    pageable);
        }
        return spotDTOList.stream().map(spotDTOMapper).toList();
    }

    private List<SpotType> getSpotTypeList(List<String> spotTypesAsString) throws InvalidInputException {
        List<SpotType> spotTypes = new ArrayList<>();
        for (String spotType : spotTypesAsString) {
            spotTypes.add(getSpotType(spotType));
        }
        return spotTypes;
    }
    private SpotType getSpotType(String spotType) throws InvalidInputException {
        return switch (spotType.trim().toLowerCase()) {
            case "calisthenics" -> SpotType.CALISTHENICS;
            case "parkour" -> SpotType.PARKOUR;
            case "freerunning" -> SpotType.FREERUNNING;
            default -> throw new InvalidInputException("Invalid SpotType");
        };
    }
}
