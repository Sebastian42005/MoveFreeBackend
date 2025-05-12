package com.example.movefree.controller.spot.attribute;

import com.example.movefree.database.spot.sport.SportRepository;
import com.example.movefree.database.spot.spottype.Attribute;
import com.example.movefree.database.spot.spottype.AttributeDTO;
import com.example.movefree.database.spot.spottype.AttributeDTOMapper;
import com.example.movefree.database.spot.spottype.AttributeRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.enums.NotFoundType;
import io.swagger.annotations.Api;
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

@Api(tags = "Spot Type")

@RestController
@RequestMapping("/api/attributes")
public class AttributesController {

    final AttributeRepository attributeRepository;
    final AttributeDTOMapper attributeDTOMapper = new AttributeDTOMapper();
    private final SportRepository sportRepository;

    public AttributesController(AttributeRepository attributeRepository, SportRepository sportRepository) {
        this.attributeRepository = attributeRepository;
        this.sportRepository = sportRepository;
    }

    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getSpotTypes() {
        List<AttributeDTO> spotTypeList = new java.util.ArrayList<>(attributeRepository.findAll().stream().map(attributeDTOMapper).toList());
        spotTypeList.sort((o1, o2) -> o1.name().compareToIgnoreCase(o2.name()));
        return ResponseEntity.ok(spotTypeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAttributeImage(@PathVariable Long id) {
        Optional<Attribute> spotType = attributeRepository.findById(id);
        if (spotType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(spotType.get().getContentType()))
                .body(spotType.get().getImage());
    }

    @PostMapping("/{sport}/{name}")
    public ResponseEntity<AttributeDTO> addSpotType(@PathVariable String sport, @PathVariable String name,
                                                    @RequestParam("image") MultipartFile image) throws IdNotFoundException {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        try {
            attribute.setImage(image.getBytes());
            attribute.setContentType(image.getContentType());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        attribute.setSport(sportRepository.findById(sport).orElseThrow(() -> new IdNotFoundException(NotFoundType.SPORT)));
        return ResponseEntity.ok(attributeDTOMapper.apply(attributeRepository.save(attribute)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpotType(@PathVariable Long id) {
        attributeRepository.deleteById(id);
        return ResponseEntity.ok(id + " deleted");
    }
}
