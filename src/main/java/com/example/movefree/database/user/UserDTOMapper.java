package com.example.movefree.database.user;

import com.example.movefree.database.spot.spot.SpotDTOMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@NoArgsConstructor
public class UserDTOMapper implements Function<User, UserDTO> {

    String username;

    @Override
    public UserDTO apply(User user) {
        if (user.getSpots() == null) user.setSpots(List.of());
        if (user.getFollower() == null) user.setFollower(List.of());
        if (user.getFollows() == null) user.setFollows(List.of());
        boolean isFollowed = false;
        if (username != null) {
            isFollowed = user.getFollower().stream().anyMatch(follower -> follower.getUsername().equals(username));
        }
        return new UserDTO(
                user.getUsername(),
                user.getSpots().stream().map(new SpotDTOMapper()).toList(),
                isFollowed,
                user.getFollower().size(),
                user.getFollows().size()
        );
    }
}
