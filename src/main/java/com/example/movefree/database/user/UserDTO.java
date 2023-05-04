package com.example.movefree.database.user;

public record UserDTO (
        String username,
        String description,
        int spotsAmount,
        double averageRating,
        boolean isFollowed,
        int follower,
        int follows
) {
}
