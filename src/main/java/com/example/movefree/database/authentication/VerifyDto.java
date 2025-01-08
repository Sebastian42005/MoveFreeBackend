package com.example.movefree.database.authentication;

import com.example.movefree.database.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerifyDto {
    private Boolean verified;
    private UserDTO userDTO;
}
