package com.example.movefree.controller.authentication;

import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.authentication.RegisterRequest;
import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.CompanyAlreadyExistsException;
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

    /**
     * 200 - Success
     * 401 - Wrong login credentials
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticationPort.login(authenticationRequest));
        } catch (WrongLoginCredentialsException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     * 400 - Invalid input
     * 409 - User already exists
     */
    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest user) {
        try {
            return ResponseEntity.ok(authenticationPort.register(user));
        } catch (InvalidInputException | UserAlreadyExistsException e) {
            return e.getResponseEntity();
        }
    }

    /**
     * 200 - Success
     */

    @PostMapping(value = "/register-company")
    public ResponseEntity<CompanyDTO> registerCompany(@RequestBody RegisterRequest user) {
        try {
            return ResponseEntity.ok(authenticationPort.registerCompany(user));
        } catch (InvalidInputException | CompanyAlreadyExistsException e) {
            return e.getResponseEntity();
        }
    }

}
