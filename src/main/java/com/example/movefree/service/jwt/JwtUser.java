package com.example.movefree.service.jwt;

import com.example.movefree.role.Role;

public record JwtUser(
        String username,
        String password,
        Role role
) {
}
