package com.example.movefree.database.authentication;


import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String token;
    private UserDTO user;
}
