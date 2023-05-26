package com.example.movefree.controller.spot;

import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.port.spot.SpotPort;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "Spot")
@RestController
@RequestMapping("/api/spot")
public class SpotController {

    final SpotPort spotPort;

    public SpotController(SpotPort spotPort) {
        this.spotPort = spotPort;
    }


    /**
     * 200 - Success
     * 400 - Invalid input
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> searchSpot(@RequestParam(defaultValue = "") String search,
                                                          @RequestParam(defaultValue = "") String spotType,
                                                          @RequestParam(defaultValue = "5") @Min(1) @Max(5) int limit,
                                                          @RequestParam(defaultValue = "") List<UUID> alreadySeenList) {
        try {
            return ResponseEntity.ok(spotPort.searchSpot(search, spotType, limit, alreadySeenList));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotDTO> getSpot(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(spotPort.getSpot(id));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - User not found
     * 403 - User forbidden
     */
    @PostMapping("/post")
    public ResponseEntity<SpotDTO> postSpot(@RequestBody PostSpotRequestBody spot, Principal principal) {
        try {
            return ResponseEntity.ok(spotPort.postSpot(spot, principal.getName()));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     */
    @GetMapping("{id}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatings(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(spotPort.getSpotRatings(id));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     * 404 - User not found
     */
    @PutMapping("/{id}/rate")
    public ResponseEntity<RatingDTO> rateSpot(@PathVariable UUID id, @RequestBody RateSpotRequestBody rateSpotRequestBody, Principal principal) {
        try {
            return ResponseEntity.ok(spotPort.rateSpot(id, rateSpotRequestBody, principal.getName()));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
