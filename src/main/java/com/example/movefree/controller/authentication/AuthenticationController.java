package com.example.movefree.controller.authentication;

import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.authentication.RegisterRequest;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.exception.UserAlreadyExistsException;
import com.example.movefree.exception.WrongLoginCredentialsException;
import com.example.movefree.port.authentication.AuthenticationPort;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Authentication")
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    final AuthenticationPort authenticationPort;

    public AuthenticationController(AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticationPort.login(authenticationRequest));
        } catch (WrongLoginCredentialsException e) {
            return e.getResponseEntity();
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest user) {
        try {
            return ResponseEntity.ok(authenticationPort.register(user));
        } catch (InvalidInputException | UserAlreadyExistsException e) {
            return e.getResponseEntity();
        }
    }
}
