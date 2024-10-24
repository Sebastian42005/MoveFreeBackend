package com.example.movefree.port.user;

import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.portclass.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserPort {

    UserDTO getUser(String username, Principal principal) throws IdNotFoundException;

    UserDTO getOwnUser(Principal principal) throws IdNotFoundException;

    List<String> searchUsers(String search, int limit);

    Picture setProfilePicture(MultipartFile image, String username) throws IOException, IdNotFoundException, InvalidMultipartFileException;

    Picture getProfilePicture(String username) throws IdNotFoundException;

    void follow(String username, Principal principal) throws IdNotFoundException;

    List<Map<String, String>> getTopUsers();

    Map<String, Object> getUserSpots(String username, int limit, List<Integer> alreadySeenList);
}
