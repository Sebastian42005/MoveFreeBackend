package com.example.movefree.service.authentication;

import com.example.movefree.config.JwtTokenUtil;
import com.example.movefree.config.ShaUtils;
import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.authentication.RegisterRequest;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserDTOMapper;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.exception.UserAlreadyExistsException;
import com.example.movefree.exception.WrongLoginCredentialsException;
import com.example.movefree.port.authentication.AuthenticationPort;
import com.example.movefree.role.Role;
import com.example.movefree.service.JwtUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthenticationService implements AuthenticationPort {

    //Repositories
    final JwtTokenUtil tokenUtil;
    final JwtUserDetailsService userDetailsService;
    final UserRepository userRepository;

    final UserDTOMapper userDTOMapper = new UserDTOMapper();

    public AuthenticationService(JwtTokenUtil tokenUtil, JwtUserDetailsService userDetailsService, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws WrongLoginCredentialsException {
        //Verify if the user logged in correctly
        final UserDetails userDetails = userDetailsService.verifyUser(authenticationRequest.getUsername(), ShaUtils.decode(authenticationRequest.getPassword()));
        //generate token for the user
        final String token = tokenUtil.generateToken(userDetails);
        //return the token
        return new AuthenticationResponse(token);
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) throws InvalidInputException, UserAlreadyExistsException {
        User user = new User();
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) throw new UserAlreadyExistsException();
        if (!Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", registerRequest.getEmail())) throw new InvalidInputException("Email is not valid");
        if (registerRequest.getUsername().trim().isEmpty()) throw new InvalidInputException("Username cannot be empty");
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", registerRequest.getPassword())) throw new InvalidInputException("Password needs a number, a letter and must minimum eight characters");
        user.setPassword(ShaUtils.decode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        //save user
        return userDTOMapper.apply(userRepository.save(user));
    }
}
