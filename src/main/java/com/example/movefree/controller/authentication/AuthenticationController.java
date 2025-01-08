package com.example.movefree.controller.authentication;

import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.authentication.RegisterRequest;
import com.example.movefree.database.authentication.VerifyDto;
import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.exception.CompanyAlreadyExistsException;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.exception.UserAlreadyExistsException;
import com.example.movefree.exception.WrongLoginCredentialsException;
import com.example.movefree.service.authentication.AuthenticationService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@Api(tags = "Authentication")
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 200 - Success
     * 401 - Wrong login credentials
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticationService.login(authenticationRequest));
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
            return ResponseEntity.ok(authenticationService.register(user));
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
            return ResponseEntity.ok(authenticationService.registerCompany(user));
        } catch (InvalidInputException | CompanyAlreadyExistsException e) {
            return e.getResponseEntity();
        }
    }

    @GetMapping("/check-login/{username}")
    public ResponseEntity<Map<String, Boolean>> checkLogin(@PathVariable String username) {
        return ResponseEntity.ok(authenticationService.checkLogin(username));
    }
    
    @GetMapping("/verify-token")
    public ResponseEntity<VerifyDto> verifyToken(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new VerifyDto(false, null));
        }
        return ResponseEntity.ok(authenticationService.verifyToken(principal));
    }
}
