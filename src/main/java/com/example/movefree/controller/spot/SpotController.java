package com.example.movefree.controller.spot;

import com.example.movefree.database.spot.location.LocationDTO;
import com.example.movefree.database.spot.location.LocationRepository;
import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.rating.RatingRepository;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOResponse;
import com.example.movefree.database.spot.spot.SpotRepository;
import com.example.movefree.database.spot.spotType.SpotType;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/spot")
public class SpotController {
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RatingRepository ratingRepository;

    //search spot with city, spot type filter and a limit
    @GetMapping("/all")
    public List<SpotDTOResponse> searchSpot(@RequestParam(defaultValue = "") List<String> cities,
                                            @RequestParam(defaultValue = "") List<SpotType> spotTypes,
                                            @RequestParam(defaultValue = "5") @Min(1) @Max(5) int limit) {
        List<SpotDTO> spotDTOList;
        if (cities.isEmpty()) {
            if (spotTypes.isEmpty()) spotDTOList = spotRepository.searchWithoutFilter(limit);
            else spotDTOList = spotRepository.searchWithSpotType(spotTypes.stream().map(Enum::ordinal).toList(), limit);
        }else {
            if (spotTypes.isEmpty()) spotDTOList = spotRepository.searchWithCity(cities.stream().map(String::toLowerCase).toList(), limit);
            else spotDTOList = spotRepository.searchWithFilter(cities.stream().map(String::toLowerCase).toList(), spotTypes.stream().map(Enum::ordinal).toList(), limit);
        }
        return spotDTOList.stream().map(SpotDTOResponse::new).toList();
    }

    @GetMapping("{id}/ratings")
    public List<RatingDTO> getRatings(@PathVariable int id) {
        SpotDTO spot = spotRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        return spot.getRatings();
    }

    @PostMapping("/post")
    public ResponseEntity<SpotDTOResponse> postSpot(@RequestBody PostSpotRequestBody spot, Principal principal) {
        //find User
        UserDTO user = userRepository.findByUsername(principal.getName()).orElseThrow();
        //create Location
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(spot.getLatitude());
        locationDTO.setLongitude(spot.getLongitude());
        locationDTO.setCity(spot.getCity());
        //create Spot
        SpotDTO spotDTO = new SpotDTO();
        spotDTO.setRatings(new ArrayList<>());
        spotDTO.setDescription(spot.getDescription());
        spotDTO.setUser(user);
        spotDTO.setLocation(locationRepository.save(locationDTO));
        spotDTO.setSpotType(spot.getSpotType());
        return ResponseEntity.ok(new SpotDTOResponse(spotRepository.save(spotDTO)));
    }

    @PutMapping("/{id}/rate")
    public RatingDTO rateSpot(@PathVariable int id, @RequestBody RateSpotRequestBody rateSpotRequestBody, Principal principal) {
        //Get User and Spot
        UserDTO userDTO = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        SpotDTO spotDTO = spotRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spot not found"));
        Optional<RatingDTO> userRating = spotDTO.getRatings().stream().filter(rating -> rating.getUser().getUsername().equals(principal.getName())).findFirst();
        //create Rating
        RatingDTO ratingDTO = new RatingDTO(rateSpotRequestBody.getMessage(), userDTO, spotDTO, rateSpotRequestBody.getStars());
        userRating.ifPresent(dto -> ratingDTO.setId(dto.getId()));
        //save Rating to Database
        RatingDTO savedRating = ratingRepository.save(ratingDTO);
        spotDTO.getRatings().add(savedRating);
        spotRepository.save(spotDTO);
        return savedRating;
    }
}
