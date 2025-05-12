package com.example.movefree.service.spot;

import com.example.movefree.database.spot.image.SpotPictureRepository;
import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.location.LocationRepository;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.rating.RatingMapper;
import com.example.movefree.database.spot.rating.RatingRepository;
import com.example.movefree.database.spot.sport.SportRepository;
import com.example.movefree.database.spot.spot.*;
import com.example.movefree.database.spot.spottype.Attribute;
import com.example.movefree.database.spot.spottype.AttributeRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SpotService {

    final SpotRepository spotRepository;
    final SpotPictureRepository spotPictureRepository;
    final UserRepository userRepository;
    final LocationRepository locationRepository;
    final RatingRepository ratingRepository;
    final AttributeRepository attributeRepository;
    final SportRepository sportRepository;
    final RatingMapper ratingMapper = new RatingMapper();

    final SpotDTOMapper spotDTOMapper = new SpotDTOMapper();

    
    public SpotDTO postSpot(PostSpotRequestBody postSpotRequestBody, String name) throws IdNotFoundException {
        //find User
        User user = userRepository.findByUsername(name).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
        List<Attribute> attributes = new ArrayList<>();
        postSpotRequestBody.getAttributes().forEach(attributeId -> attributes.add(attributeRepository.findByName(attributeId).orElseThrow()));
        //create Location
        Location location = new Location();
        location.setLatitude(postSpotRequestBody.getLatitude());
        location.setLongitude(postSpotRequestBody.getLongitude());
        location.setCity(postSpotRequestBody.getCity());
        //create Spot
        Spot spot = new Spot();
        spot.setRatings(new ArrayList<>());
        spot.setTitle(postSpotRequestBody.getTitle());
        spot.setDescription(postSpotRequestBody.getDescription());
        spot.setUser(user);
        spot.setLocation(locationRepository.save(location));
        spot.setSport(sportRepository.findById(postSpotRequestBody.getSport()).orElseThrow());
        spot.setAttributes(attributes);
        spot.setCreatedAt(Instant.now());
        return spotDTOMapper.apply(spotRepository.save(spot));
    }

    
    public RatingDTO rateSpot(Integer id, RateSpotRequestBody rateSpotRequestBody, String name) throws IdNotFoundException {
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

    
    public List<RatingDTO> getSpotRatings(Integer spotId) throws IdNotFoundException {
        Spot spot = spotRepository.findById(spotId).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        return spot.getRatings().stream().map(ratingMapper).toList();
    }
    
    public SpotDTO getSpot(Integer id) throws IdNotFoundException {
        return spotDTOMapper.apply(spotRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT)));
    }

    
    public void deleteSpot(Integer id, Principal principal) throws IdNotFoundException, UserForbiddenException {
        Spot spot = spotRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        if (!spot.getUser().getUsername().equals(principal.getName())) throw new UserForbiddenException();
        spotPictureRepository.deleteAll(spot.getPictures());
        spotRepository.delete(spot);
    }

    
    public void saveSpot(Integer id, Principal principal) throws IdNotFoundException {
        Spot spot = spotRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
        if (user.getSavedSpots().stream().anyMatch(savedSpot -> savedSpot.getId().equals(id))) {
            spot.getSavedBy().remove(user);
            user.getSavedSpots().remove(spotRepository.save(spot));
            userRepository.save(user);
        } else {
            spot.getSavedBy().add(user);
            user.getSavedSpots().add(spotRepository.save(spot));
            userRepository.save(user);
        }
    }

    
    public Map<String, Object> getSavedSpots(Principal principal, List<Integer> alreadySeenList, int limit) {
        if (alreadySeenList.isEmpty()) alreadySeenList = List.of(0);
        Pageable pageable = PageRequest.of(0, limit + 1);
        List<Spot> spots = userRepository.getUserSavedSpots(principal.getName(), alreadySeenList, pageable);
        return spotListToMap(spots, limit);
    }

    
    public boolean isSaved(Integer id, Principal principal) {
        return spotRepository.isSaved(id, principal.getName());
    }

    private Map<String, Object> spotListToMap(List<Spot> spots, int limit) {
        Map<String, Object> map = new HashMap<>();
        Boolean hasMore = spots.size() > limit;
        if (Boolean.TRUE.equals(hasMore)) spots.remove(spots.size() - 1);
        map.put("spots", spots.stream().map(spotDTOMapper).toList());
        map.put("hasMore", hasMore);
        return map;
    }

    public Map<String, Object> searchSpot(Double minRating, String search, String sport, String city, List<Long> attributeIds, int limit, List<Integer> alreadySeenList) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        if (alreadySeenList.isEmpty()) alreadySeenList = List.of(0);
        List<Spot> spotList;
        if (attributeIds == null || attributeIds.isEmpty()) {
            spotList = spotRepository.getSpotsWithoutAttributes(minRating, sport, search, city, alreadySeenList, pageable);
        } else {
            spotList = spotRepository.getSpotsWithAttributes(minRating, sport, search, city, attributeIds, alreadySeenList, pageable);
        }
        return spotListToMap(spotList, limit);
    }

    public List<MarkerDto> getSpotMarkers(Double minRating, String sport, List<Long> attributeIds, String search, String city) {
        if (attributeIds == null || attributeIds.isEmpty()) {
            return spotRepository.getMarkersWithoutAttributes(minRating, sport, search, city);
        } else {
            return spotRepository.getMarkersWithAttributes(minRating, sport, search, city, attributeIds);
        }
    }
}
