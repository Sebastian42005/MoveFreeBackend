package com.example.movefree.service.location;

import com.example.movefree.database.spot.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public List<String> getAllCities() {
        return locationRepository.getAllCities();
    }
}
