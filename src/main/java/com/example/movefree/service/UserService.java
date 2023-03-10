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
import java.util.List;

@Service
public class UserService implements UserPort {

    final UserRepository userRepository;

    final UserDTOMapper userDTOMapper = new UserDTOMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUser(String username) throws IdNotFoundException {
        return userDTOMapper.apply(findUser(username));
    }

    @Override
    public List<UserDTO> searchUsers(String search, int limit) {
        return userRepository.search(search, limit).stream().map(userDTOMapper).toList();
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

    private User findUser(String username) throws IdNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(IdNotFoundException.get(NotFoundType.USER));
    }
}
