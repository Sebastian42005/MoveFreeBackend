package com.example.movefree.service.jwt;

public record JwtUser(
        String username,
        String password,
        String role
) {
}
