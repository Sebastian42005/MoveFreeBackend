package com.example.movefree.service;

import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserDTOMapper;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.exception.enums.MultipartFileExceptionType;
import com.example.movefree.exception.enums.NotFoundType;
import com.example.movefree.file.ImageReader;
import com.example.movefree.port.user.UserPort;
import com.example.portclass.Picture;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
public class UserService implements UserPort {

    final UserRepository userRepository;

    UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUser(String username, Principal principal) throws IdNotFoundException {
        if (principal != null) {
            userDTOMapper = new UserDTOMapper(principal.getName());
        }else userDTOMapper = new UserDTOMapper();
        return userDTOMapper.apply(findUser(username));
    }

    @Override
    public UserDTO getOwnUser(Principal principal) throws IdNotFoundException {
        userDTOMapper = new UserDTOMapper(principal.getName());
        return userDTOMapper.apply(findUser(principal.getName()));
    }

    @Override
    public List<String> searchUsers(String search, int limit) {
        return userRepository.search(search, limit);
    }

    @Override
    public Picture setProfilePicture(MultipartFile image, String username) throws IOException, IdNotFoundException, InvalidMultipartFileException {
        // check
        String contentType = image.getContentType();
        if (contentType == null) throw new InvalidMultipartFileException(MultipartFileExceptionType.NO_CONTENT);
        if (!Picture.checkIfValidImage(image)) throw new InvalidMultipartFileException(MultipartFileExceptionType.NO_IMAGE);

        // setup
        User user = findUser(username);
        user.setProfilePicture(image.getBytes());
        user.setContentType(image.getContentType());

        // save
        userRepository.save(user);

        // return
        return new Picture(MediaType.valueOf(contentType), image.getBytes());
    }

    @Override
    public Picture getProfilePicture(String username) throws IdNotFoundException {
        User user = findUser(username);
        if (user.getProfilePicture() == null) {
            return ImageReader.getProfilePicture();
        }
        return new Picture(MediaType.valueOf(user.getContentType()), user.getProfilePicture());
    }

    @Override
    public void follow(String username, Principal principal) throws IdNotFoundException {
        User followUser = findUser(username);
        User ownUser = findUser(principal.getName());

        if (followUser.getFollower().contains(ownUser)) {
            followUser.getFollower().remove(ownUser);
        } else {
            followUser.getFollower().add(ownUser);
        }

        userRepository.save(followUser);
    }

    @Override
    public List<String> getTopUsers() {
        List<User> users = new java.util.ArrayList<>(List.copyOf(userRepository.findAll()));
        users.sort((o1, o2) -> o2.getFollower().size() - o1.getFollower().size());
        return users.stream().map(User::getUsername).limit(5).toList();
    }

    private User findUser(String username) throws IdNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
    }
}
