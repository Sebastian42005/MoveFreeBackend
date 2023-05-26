package com.example.movefree.database.user;

public record UserDTO (
        String username,
        String role,
        String description,
        int spotsAmount,
        int follower,
        int follows
) {
}
