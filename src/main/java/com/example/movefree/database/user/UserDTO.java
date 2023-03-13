package com.example.movefree.database.user;

import com.example.movefree.database.spot.spot.SpotDTO;

import java.util.List;

public record UserDTO (
        String username,
        List<SpotDTO> spots,
        boolean isFollowed,
        int follower,
        int follows
) {
}
