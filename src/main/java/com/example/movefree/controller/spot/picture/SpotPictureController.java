package com.example.movefree.controller.spot.picture;

import com.example.movefree.database.spot.image.SpotPictureDTO;
import com.example.movefree.database.spot.image.SpotPictureRepository;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Api(tags = "Spot Pictures")
@RestController
@RequestMapping("/api/spot")
public class SpotPictureController {

    @Autowired
    SpotRepository spotRepository;

    @Autowired
    SpotPictureRepository spotPictureRepository;

    @PutMapping("/{id}/pictures")
    public void uploadPictures(@PathVariable int id, @RequestParam("images") List<MultipartFile> images, Principal principal) throws IOException {
        SpotDTO spotDTO = getSpot(id, principal.getName());
        if (spotDTO.getPictures().size() + images.size() > 10) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        for (MultipartFile image : images) {
            SpotPictureDTO pictureDTO = new SpotPictureDTO();
            pictureDTO.setPicture(image.getBytes());
            pictureDTO.setContentType(image.getContentType());
            pictureDTO.setSpot(spotDTO);
            spotDTO.getPictures().add(spotPictureRepository.save(pictureDTO));
        }
        spotRepository.save(spotDTO);
    }

    @GetMapping("/pictures/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable int id) {
        SpotPictureDTO pictureDTO = findPicture(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(pictureDTO.getContentType()))
                .body(pictureDTO.getPicture());
    }

    private SpotPictureDTO findPicture(int id) {
        return spotPictureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    private SpotDTO getSpot(int id, String username) {
        SpotDTO spotDTO = spotRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!spotDTO.getUser().getUsername().equals(username)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return spotDTO;
    }
}
