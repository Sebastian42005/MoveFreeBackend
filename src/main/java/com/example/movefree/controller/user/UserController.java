package com.example.movefree.controller.user;

import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserDTOResponse;
import com.example.movefree.database.user.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Api(tags = "User")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{username}")
    public ResponseEntity<UserDTOResponse> getUser(@PathVariable String username) {
        return ResponseEntity.ok(new UserDTOResponse(findUser(username)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTOResponse>> searchUser(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "5") @Max(99) int limit) {
        return ResponseEntity.ok(userRepository.search(search, limit).stream().map(UserDTOResponse::new).toList());
    }

    @PutMapping("/profile")
    public ResponseEntity<byte[]> setProfilePicture(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        UserDTO userDTO = findUser(principal.getName());
        userDTO.setProfilePicture(image.getBytes());
        userDTO.setContentType(image.getContentType());
        userRepository.save(userDTO);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(userDTO.getContentType()))
                .body(userDTO.getProfilePicture());
    }

    //get profile picture of company member
    @GetMapping("/{username}/profile")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        UserDTO userDTO = findUser(username);
        if (userDTO.getProfilePicture() == null) return ResponseEntity.ok(null);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(userDTO.getContentType()))
                .body(userDTO.getProfilePicture());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTOResponse> getUser(Principal principal) {
        UserDTO userDTO = findUser(principal.getName());
        return ResponseEntity.ok(new UserDTOResponse(userDTO));
    }

    private UserDTO findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
