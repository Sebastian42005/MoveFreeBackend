package com.example.movefree.controller.user;

import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.port.user.UserPort;
import com.example.portclass.Picture;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Api(tags = "User")
@RestController
@RequestMapping("/api/user")
public class UserController {
    final UserPort userPort;

    public UserController(UserPort userPort) {
        this.userPort = userPort;
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username, Principal principal) {
        try {
            return ResponseEntity.ok(userPort.getUser(username, principal));
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    @PutMapping("/{username}/follow")
    public ResponseEntity<String> follow(@PathVariable String username, Principal principal) {
        try {
            userPort.follow(username, principal);
            return ResponseEntity.ok(username + " followed");
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     */
    @GetMapping("/search")
    public ResponseEntity<List<String>> searchUser(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "5") @Max(99) int limit) {
        return ResponseEntity.ok(userPort.searchUsers(search, limit));
    }

    /**
     * 200 - Success
     * 404 - User not found
     * 400 - Image invalid
     */
    @PutMapping("/profile")
    public ResponseEntity<byte[]> setProfilePicture(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        try {
            Picture profilePicture = userPort.setProfilePicture(image, principal.getName());
            return ResponseEntity.ok().contentType(profilePicture.contentType()).body(profilePicture.content());
        }catch (IdNotFoundException | InvalidMultipartFileException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/{username}/profile")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        Picture profilePicture;
        try {
            profilePicture = userPort.getProfilePicture(username);
            return ResponseEntity.ok().contentType(profilePicture.contentType()).body(profilePicture.content());
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/own/profile")
    public ResponseEntity<byte[]> getOwnProfilePicture(Principal principal) {
        Picture profilePicture;
        try {
            profilePicture = userPort.getProfilePicture(principal.getName());
            return ResponseEntity.ok().contentType(profilePicture.contentType()).body(profilePicture.content());
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/own")
    public ResponseEntity<UserDTO> getUser(Principal principal) {
        try {
            return ResponseEntity.ok(userPort.getOwnUser(principal));
        }catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }
}
