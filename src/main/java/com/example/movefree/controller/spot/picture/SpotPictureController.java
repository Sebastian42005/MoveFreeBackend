package com.example.movefree.controller.spot.picture;

import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.PictureOverflowException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.port.spot.SpotPicturePort;
import com.example.movefree.portclass.Picture;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Api(tags = "Spot Pictures")
@RestController
@RequestMapping("/api/spot")
public class SpotPictureController {

    final SpotPicturePort spotPicturePort;

    public SpotPictureController(SpotPicturePort spotPicturePort) {
        this.spotPicturePort = spotPicturePort;
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     * 404 - User not found
     * 401 - Unauthorized
     * 413 - Too many pictures uploaded
     */
    @PutMapping("/{id}/images")
    public ResponseEntity<SpotDTO> uploadPictures(@PathVariable UUID id, @RequestParam("images") List<MultipartFile> images, Principal principal) {
        try {
            return ResponseEntity.ok(spotPicturePort.uploadPicture(id, images, principal.getName()));
        }catch (IdNotFoundException | UserForbiddenException | PictureOverflowException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     */
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable UUID id) {
        try {
            Picture picture = spotPicturePort.getPicture(id);
            return ResponseEntity.ok()
                    .contentType(picture.contentType())
                    .body(picture.content());
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
