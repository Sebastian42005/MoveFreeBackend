package com.example.movefree.database.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    String username;
    String password;
    String email;
}
