package com.example.movefree.controller.spot.type;

import com.example.movefree.database.spot.spottype.SpotType;
import com.example.movefree.database.spot.spottype.SpotTypeDTO;
import com.example.movefree.database.spot.spottype.SpotTypeDTOMapper;
import com.example.movefree.database.spot.spottype.SpotTypeRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spot/type")
public class SpotTypeController {

    final SpotTypeRepository spotTypeRepository;
    final SpotTypeDTOMapper spotTypeDTOMapper = new SpotTypeDTOMapper();

    public SpotTypeController(SpotTypeRepository spotTypeRepository) {
        this.spotTypeRepository = spotTypeRepository;
    }

    @GetMapping
    public ResponseEntity<List<SpotTypeDTO>> getSpotTypes() {
        List<SpotTypeDTO> spotTypeList = new java.util.ArrayList<>(spotTypeRepository.findAll().stream().map(spotTypeDTOMapper).toList());
        spotTypeList.sort((o1, o2) -> o1.name().compareToIgnoreCase(o2.name()));
        return ResponseEntity.ok(spotTypeList);
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getSpotTypeImage(@PathVariable String name) {
        Optional<SpotType> spotType = spotTypeRepository.findById(name);
        if (spotType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(spotType.get().getContentType()))
                .body(spotType.get().getImage());
    }

    @PostMapping("/{name}")
    public ResponseEntity<SpotTypeDTO> addSpotType(@PathVariable String name,
                                              @RequestParam("image") MultipartFile image) {
        SpotType spotType = new SpotType();
        spotType.setName(name);
        try {
            spotType.setImage(image.getBytes());
            spotType.setContentType(image.getContentType());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(spotTypeDTOMapper.apply(spotTypeRepository.save(spotType)));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteSpotType(@PathVariable String name) {
        spotTypeRepository.deleteById(name);
        return ResponseEntity.ok(name + " deleted");
    }
}
