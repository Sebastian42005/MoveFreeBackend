package com.example.movefree.database.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;
}
