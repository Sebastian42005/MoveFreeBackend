package com.example.movefree.controller.spot;

import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.spot.MarkerDto;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import com.example.movefree.service.UserService;
import com.example.movefree.service.spot.RecommendationService;
import com.example.movefree.service.spot.SpotService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Api(tags = "Spot")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/spot")
public class SpotController {

    private final SpotService spotService;
    private final RecommendationService recommendationService;
    private final UserService userService;

    /**
     * 200 - Success
     * 400 - Invalid input
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> searchSpot(@RequestParam(required = false) Double minRating,
                                                          @RequestParam(required = false) String sport,
                                                          @RequestParam(required = false) List<Long> attributeIds,
                                                          @RequestParam(required = false) String search,
                                                          @RequestParam(required = false) String city,
                                                          @RequestParam(defaultValue = "20") @Min(1) @Max(50) int limit,
                                                          @RequestParam(defaultValue = "") List<Integer> alreadySeenList) {
        return ResponseEntity.ok(spotService.searchSpot(minRating, search, sport, city, attributeIds, limit, alreadySeenList));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<SpotDTO>> getRecommendedSpots(Principal principal) throws IdNotFoundException {
        return ResponseEntity.ok(recommendationService.getRecommendationsForUser(userService.getUser(principal), 20));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotDTO> getSpot(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(spotService.getSpot(id));
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
            return ResponseEntity.ok(spotService.postSpot(spot, principal.getName()));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     * 403 - User forbidden
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Integer id, Principal principal) {
        try {
            spotService.deleteSpot(id, principal);
            return ResponseEntity.ok().build();
        } catch (IdNotFoundException | UserForbiddenException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     */
    @GetMapping("{id}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatings(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(spotService.getSpotRatings(id));
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
    public ResponseEntity<RatingDTO> rateSpot(@PathVariable Integer id, @RequestBody RateSpotRequestBody rateSpotRequestBody, Principal principal) {
        try {
            return ResponseEntity.ok(spotService.rateSpot(id, rateSpotRequestBody, principal.getName()));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     * 403 - User forbidden
     */
    @PutMapping("/{id}/save")
    public ResponseEntity<Map<String, String>> saveSpot(@PathVariable Integer id, Principal principal) {
        try {
            spotService.saveSpot(id, principal);
            return ResponseEntity.ok(Map.of("message", "Spot saved"));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     * 403 - User forbidden
     */
    @GetMapping("/saved")
    public ResponseEntity<Map<String, Object>> getSavedSpots(Principal principal,
                                                       @RequestParam(defaultValue = "20") @Min(1) @Max(20) int limit,
                                                       @RequestParam(defaultValue = "") List<Integer> alreadySeenList) {
        return ResponseEntity.ok(spotService.getSavedSpots(principal, alreadySeenList, limit));
    }

    /**
     * 200 - Success
     */
    @GetMapping("/{id}/isSaved")
    public ResponseEntity<Map<String, Boolean>> isSaved(@PathVariable Integer id, Principal principal) {
        if (principal == null)
            return ResponseEntity.ok(Map.of("isSaved", false));
        return ResponseEntity.ok(Map.of("isSaved", spotService.isSaved(id, principal)));
    }

    @GetMapping("/markers")
    public ResponseEntity<List<MarkerDto>> getSpotMarkers(
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) List<Long> attributeIds,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String city) {
        return ResponseEntity.ok(
                spotService.getSpotMarkers(minRating, sport, attributeIds, search, city)
        );
    }
}
