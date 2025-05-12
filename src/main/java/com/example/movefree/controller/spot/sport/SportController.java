package com.example.movefree.controller.spot.sport;

import com.example.movefree.database.spot.sport.Sport;
import com.example.movefree.database.spot.sport.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sports")
public class SportController {
    final SportRepository sportRepository;

    @GetMapping
    public ResponseEntity<List<Sport>> getSports() {
        return ResponseEntity.ok(sportRepository.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Sport> getSport(@PathVariable String name) {
        Optional<Sport> sport = sportRepository.findById(name);
        return sport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sport> createSport(@RequestBody Sport sport) {
        Sport sportCopy = new Sport();
        sportCopy.setName(sportCopy.getName());
        sportCopy.setSymbol(sportCopy.getSymbol());
        sportCopy.setColor(sportCopy.getColor());
        return ResponseEntity.ok(sportRepository.save(sport));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteSport(@PathVariable String name) {
        sportRepository.deleteById(name);
        return ResponseEntity.ok(name + " deleted");
    }
}
