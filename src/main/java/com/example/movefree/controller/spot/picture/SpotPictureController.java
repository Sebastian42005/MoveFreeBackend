package com.example.movefree.controller.spot.picture;

import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.PictureOverflowException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.portclass.Picture;
import com.example.movefree.service.spot.SpotPictureService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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

@Api(tags = "Spot Pictures")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spot")
public class SpotPictureController {

    final SpotPictureService spotPictureService;
    
    /**
     * 200 - Success
     * 404 - Spot not found
     * 404 - User not found
     * 401 - Unauthorized
     * 413 - Too many pictures uploaded
     */
    @PutMapping("/{id}/images")
    public ResponseEntity<SpotDTO> uploadPictures(@PathVariable Integer id, @RequestParam("images") List<MultipartFile> images, Principal principal) {
        try {
            return ResponseEntity.ok(spotPictureService.uploadPicture(id, images, principal.getName()));
        }catch (IdNotFoundException | UserForbiddenException | PictureOverflowException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - Spot not found
     */
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable Integer id) {
        try {
            Picture picture = spotPictureService.getPicture(id);
            return ResponseEntity.ok()
                    .contentType(picture.contentType())
                    .body(picture.content());
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
