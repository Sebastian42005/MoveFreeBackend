package com.example.movefree.port.authentication;

import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.authentication.RegisterRequest;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.exception.UserAlreadyExistsException;
import com.example.movefree.exception.WrongLoginCredentialsException;

public interface AuthenticationPort {

    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws WrongLoginCredentialsException;
    UserDTO register(RegisterRequest userDTO) throws InvalidInputException, UserAlreadyExistsException;
}
