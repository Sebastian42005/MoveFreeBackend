package com.example.movefree.service.spot;

import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.rating.RatingRepository;
import com.example.movefree.database.spot.sport.Sport;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOMapper;
import com.example.movefree.database.spot.spot.SpotRepository;
import com.example.movefree.database.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final SpotRepository spotRepository;
    private final RatingRepository ratingRepository;
    private final SpotDTOMapper spotDTOMapper = new SpotDTOMapper();

    public List<SpotDTO> getRecommendationsForUser(User user, int limit) {
        List<Sport> favoriteSports = user.getFavoriteSports();

        // Spots aus den Lieblingssportarten
        List<Spot> fromFavorites = spotRepository.findTopBySportInOrderByRatingsDesc(favoriteSports, PageRequest.of(0, limit));

        // Spots aus bewerteten Sportarten
        List<Rating> ratings = ratingRepository.findByUser(user);
        Map<Sport, Long> ratedSportFrequency = ratings.stream()
                .map(r -> r.getSpot().getSport())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Sport> mostRatedSports = ratedSportFrequency.entrySet().stream()
                .sorted(Map.Entry.<Sport, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();

        List<Spot> fromRated = spotRepository.findTopBySportInOrderByRatingsDesc(mostRatedSports, PageRequest.of(0, limit));

        // Kombinieren und Duplikate entfernen
        List<Spot> combined = new ArrayList<>();
        combined.addAll(fromFavorites);
        combined.addAll(fromRated);

        // Entferne Spots, die der User selbst erstellt oder bereits bewertet hat
        List<Spot> createdByUser = user.getSpots();
        List<Spot> ratedByUser = ratings.stream().map(Rating::getSpot).toList();

        return combined.stream()
                .filter(s -> !createdByUser.contains(s) && !ratedByUser.contains(s))
                .limit(limit)
                .map(spotDTOMapper)
                .toList();
    }
}
