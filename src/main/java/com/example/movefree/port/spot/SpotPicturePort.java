package com.example.movefree.port.spot;

import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.PictureOverflowException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.movefree.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SpotPicturePort {
    Picture getPicture(Integer id) throws IdNotFoundException;
    SpotDTO uploadPicture(Integer id, List<MultipartFile> images, String name) throws UserForbiddenException, IdNotFoundException, PictureOverflowException;
}
