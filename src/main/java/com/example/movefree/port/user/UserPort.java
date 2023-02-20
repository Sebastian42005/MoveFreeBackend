package com.example.movefree.port.user;

import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserPort {

    UserDTO getUser(String username) throws IdNotFoundException;

    List<UserDTO> searchUsers(String search, int limit);

    Picture setProfilePicture(MultipartFile image, String username) throws IOException, IdNotFoundException, InvalidMultipartFileException;

    Picture getProfilePicture(String username) throws IdNotFoundException;
}
