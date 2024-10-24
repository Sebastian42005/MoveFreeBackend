package com.example.movefree.database.user;

import com.example.movefree.role.Role;

public record UserDTO (
        String username,
        Role role,
        String description,
        int spotsAmount,
        int follower,
        int follows
) {
}
