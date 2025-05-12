package com.example.movefree.service;

import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOMapper;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserDTOMapper;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.exception.enums.MultipartFileExceptionType;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.file.ImageReader;
import com.example.movefree.portclass.Picture;
import com.example.movefree.service.user.UserProfilePicture;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    final UserRepository userRepository;

    UserDTOMapper userDTOMapper;

    SpotDTOMapper spotDTOMapper = new SpotDTOMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public UserDTO getUser(String username, Principal principal) throws IdNotFoundException {
        if (principal != null) {
            userDTOMapper = new UserDTOMapper(principal.getName());
        }else userDTOMapper = new UserDTOMapper();
        return findUser(username);
    }

    
    public UserDTO getOwnUser(Principal principal) throws IdNotFoundException {
        userDTOMapper = new UserDTOMapper(principal.getName());
        return findUser(principal.getName());
    }

    
    public List<String> searchUsers(String search, int limit) {
        return userRepository.search(search, limit);
    }

    
    public Picture setProfilePicture(MultipartFile image, String username) throws IOException, IdNotFoundException, InvalidMultipartFileException {
        // check
        String contentType = image.getContentType();
        if (contentType == null) throw new InvalidMultipartFileException(MultipartFileExceptionType.NO_CONTENT);
        if (!Picture.checkIfValidImage(image)) throw new InvalidMultipartFileException(MultipartFileExceptionType.NO_IMAGE);

        // setup
        User user = findUserFromDatabase(username);
        user.setProfilePicture(image.getBytes());
        user.setContentType(image.getContentType());

        // save
        userRepository.save(user);

        // return
        return new Picture(MediaType.valueOf(contentType), image.getBytes());
    }

    
    public Picture getProfilePicture(String username, boolean dark) throws IdNotFoundException {
        UserProfilePicture userProfilePicture = findUserProfilePicture(username);
        if (userProfilePicture.content() == null) {
            return ImageReader.getProfilePicture(dark);
        }
        return new Picture(MediaType.valueOf(userProfilePicture.contentType()), userProfilePicture.content());
    }

    
    public void follow(String username, Principal principal) throws IdNotFoundException {
        User followUser = findUserFromDatabase(username);
        User ownUser = findUserFromDatabase(principal.getName());

        if (followUser.getFollower().contains(ownUser)) {
            followUser.getFollower().remove(ownUser);
        } else {
            followUser.getFollower().add(ownUser);
        }

        userRepository.save(followUser);
    }

    public User getUser(Principal principal) throws IdNotFoundException {
        return userRepository.findByUsername(principal.getName()).orElseThrow(() -> new IdNotFoundException(NotFoundType.USER));
    }
    
    public List<Map<String, String>> getTopUsers() {
        List<User> users = new java.util.ArrayList<>(List.copyOf(userRepository.findAll()));
        users.sort((o1, o2) -> o2.getFollower().size() - o1.getFollower().size());
        return users.stream().map(user -> Map.of("username", user.getUsername())).limit(5).toList();
    }

    
    public Map<String, Object> getUserSpots(String username, int limit, List<Integer> alreadySeenList) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        if (alreadySeenList.isEmpty()) alreadySeenList = List.of(0);
        List<SpotDTO> spotDTOList = new ArrayList<>(userRepository.getUserSpots(username, alreadySeenList, pageable).stream().map(spotDTOMapper).toList());
        Map<String, Object> map = new HashMap<>();
        Boolean hasMore = spotDTOList.size() > limit;
        if (Boolean.TRUE.equals(hasMore)) {
            spotDTOList.remove(spotDTOList.size() - 1);
        }
        map.put("spots", spotDTOList);
        map.put("hasMore", hasMore);
        return map;
    }

    private UserDTO findUser(String username) throws IdNotFoundException {
        return userRepository.findDTOByUsername(username).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
    }

    private UserProfilePicture findUserProfilePicture(String username) throws IdNotFoundException {
        return userRepository.getUserProfilePictureByUsername(username).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
    }

    private User findUserFromDatabase(String username) throws IdNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
    }
}
