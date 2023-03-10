package com.example.movefree.service.spot;

import com.example.movefree.database.spot.image.SpotPicture;
import com.example.movefree.database.spot.image.SpotPictureRepository;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spot.SpotRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.PictureOverflowException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.port.spot.SpotPicturePort;
import com.example.portclass.Picture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SpotPictureService implements SpotPicturePort {

    final SpotPictureRepository spotPictureRepository;

    final SpotRepository spotRepository;

    public SpotPictureService(SpotPictureRepository spotPictureRepository, SpotRepository spotRepository) {
        this.spotPictureRepository = spotPictureRepository;
        this.spotRepository = spotRepository;
    }

    @Override
    public Picture getPicture(UUID id) throws IdNotFoundException {
        SpotPicture spotPicture = findPicture(id);
        return new Picture(MediaType.valueOf(spotPicture.getContentType()), spotPicture.getPicture());
    }

    @Override
    public void uploadPicture(UUID id, List<MultipartFile> images, String name) throws PictureOverflowException, IdNotFoundException, UserForbiddenException {
        Spot spot = getSpot(id, name);
        if (spot.getPictures().size() + images.size() > 10) throw new PictureOverflowException();
        for (MultipartFile image : images) {
            try {
                SpotPicture pictureDTO = new SpotPicture();
                pictureDTO.setPicture(image.getBytes());
                pictureDTO.setContentType(image.getContentType());
                pictureDTO.setSpot(spot);
                spot.getPictures().add(spotPictureRepository.save(pictureDTO));
            }catch (IOException exception) {
                log.error("IOException: " + exception.getMessage());
            }
        }
        spotRepository.save(spot);
    }

    private SpotPicture findPicture(UUID id) throws IdNotFoundException{
        return spotPictureRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.PICTURE));
    }
    private Spot getSpot(UUID id, String username) throws IdNotFoundException, UserForbiddenException {
        Spot spotDTO = spotRepository.findById(id).orElseThrow(IdNotFoundException.get(NotFoundType.SPOT));
        if (!spotDTO.getUser().getUsername().equals(username)) throw new UserForbiddenException();
        return spotDTO;
    }
}
