package com.example.movefree.database.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthenticationRequest {
    private String username;
    private String password;
}
