package com.example.movefree.controller.user;

import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.IdNotFoundException;
import com.example.movefree.exception.InvalidMultipartFileException;
import com.example.movefree.portclass.Picture;
import com.example.movefree.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;

@Api(tags = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    final UserService userService;
    
    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username, Principal principal) {
        try {
            UserDTO user = userService.getUser(username, principal);
            return ResponseEntity.ok(user);
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    @GetMapping("/{username}/spots")
    public ResponseEntity<Map<String, Object>> getUserSpots(@PathVariable String username,
                                                            @RequestParam(defaultValue = "20") @Max(99) int limit,
                                                            @RequestParam(defaultValue = "") List<Integer> alreadySeenList) {
        return ResponseEntity.ok(userService.getUserSpots(username, limit, alreadySeenList));
    }

    @PutMapping("/own/follow/{username}")
    public ResponseEntity<String> follow(@PathVariable String username, Principal principal) {
        try {
            userService.follow(username, principal);
            return ResponseEntity.ok(username + " followed");
        } catch (IdNotFoundException e) {
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
        return ResponseEntity.ok(userService.searchUsers(search, limit));
    }

    /**
     * 200 - Success
     * 404 - User not found
     * 400 - Image invalid
     */
    @PutMapping("/own/profile")
    public ResponseEntity<byte[]> setProfilePicture(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        try {
            Picture profilePicture = userService.setProfilePicture(image, principal.getName());
            return ResponseEntity.ok().contentType(profilePicture.contentType()).body(profilePicture.content());
        } catch (IdNotFoundException | InvalidMultipartFileException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/{username}/profile")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username, @RequestParam(defaultValue = "false") boolean dark) {
        Picture profilePicture;
        try {
            profilePicture = userService.getProfilePicture(username, dark);
            return ResponseEntity.ok().contentType(profilePicture.contentType()).body(profilePicture.content());
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    @GetMapping("/top")
    public ResponseEntity<List<Map<String, String>>> getTopUsers() {
        return ResponseEntity.ok(userService.getTopUsers());
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/own")
    public ResponseEntity<UserDTO> getUser(Principal principal) {
        try {
            return ResponseEntity.ok(userService.getOwnUser(principal));
        } catch (IdNotFoundException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 404 - User not found
     */
    @GetMapping("/own/name")
    public ResponseEntity<Map<String, String>> getUsername(Principal principal) {
        return ResponseEntity.ok(Map.of("username", principal.getName()));
    }
}
