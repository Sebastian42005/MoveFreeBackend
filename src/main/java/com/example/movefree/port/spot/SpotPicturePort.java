package com.example.movefree.port.spot;

import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.PictureOverflowException;
import com.example.movefree.exception.UserForbiddenException;
import com.example.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface SpotPicturePort {
    Picture getPicture(UUID id) throws IdNotFoundException;
    void uploadPicture(UUID id, List<MultipartFile> images, String name) throws UserForbiddenException, IdNotFoundException, PictureOverflowException;
}
