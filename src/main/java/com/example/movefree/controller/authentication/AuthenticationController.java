package com.example.movefree.controller.authentication;

import com.example.movefree.config.ShaUtils;
import com.example.movefree.config.JwtTokenUtil;
import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.role.Role;
import com.example.movefree.service.JwtUserDetailsService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@Api(tags = "Authentication")
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    //Repositories
    final JwtTokenUtil tokenUtil;
    final JwtUserDetailsService userDetailsService;
    final UserRepository userRepository;

    public AuthenticationController(JwtTokenUtil tokenUtil, JwtUserDetailsService userDetailsService, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        //Verify if the user logged in correctly
        final UserDetails userDetails = userDetailsService.verifyUser(authenticationRequest.getUsername(), ShaUtils.decode(authenticationRequest.getPassword()));
        //generate token for the user
        final String token = tokenUtil.generateToken(userDetails);
        //return the token
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        //check if user already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        //check if email is valid
        if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", user.getEmail())) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Pick a real email");
        //check if username is not empty
        if (user.getUsername().trim().isEmpty()) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Username can't be empty");
        //check if password is secure enough
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", user.getPassword())) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Password needs a number, a letter and must minimum eight characters");
        //decode user password
        user.setPassword(ShaUtils.decode(user.getPassword()));
        //set user role to user
        user.setRole(Role.USER);
        //save user
        return ResponseEntity.ok(userRepository.save(user));
    }
}
