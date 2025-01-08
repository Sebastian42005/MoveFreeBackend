package com.example.movefree.service.authentication;

import com.example.movefree.config.JwtTokenUtil;
import com.example.movefree.config.ShaUtils;
import com.example.movefree.database.authentication.AuthenticationRequest;
import com.example.movefree.database.authentication.AuthenticationResponse;
import com.example.movefree.database.authentication.RegisterRequest;
import com.example.movefree.database.authentication.VerifyDto;
import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyDTOMapper;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserDTOMapper;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.CompanyAlreadyExistsException;
import com.example.movefree.exception.InvalidInputException;
import com.example.movefree.exception.UserAlreadyExistsException;
import com.example.movefree.exception.WrongLoginCredentialsException;
import com.example.movefree.role.Role;
import com.example.movefree.service.jwt.JwtUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {

    //Repositories
    final JwtTokenUtil tokenUtil;
    final JwtUserDetailsService userDetailsService;
    final UserRepository userRepository;
    final CompanyRepository companyRepository;
    final UserDTOMapper userDTOMapper = new UserDTOMapper();
    final CompanyDTOMapper companyDTOMapper = new CompanyDTOMapper();

    public AuthenticationService(JwtTokenUtil tokenUtil, JwtUserDetailsService userDetailsService, UserRepository userRepository, CompanyRepository companyRepository) {
        this.tokenUtil = tokenUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws WrongLoginCredentialsException {
        final UserDetails userDetails = userDetailsService.verifyUser(authenticationRequest.getUsername(), ShaUtils.decode(authenticationRequest.getPassword()));
        final String token = tokenUtil.generateToken(userDetails);
        final User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(WrongLoginCredentialsException::new);
        return new AuthenticationResponse(token, userDTOMapper.apply(user));
    }

    
    public UserDTO register(RegisterRequest registerRequest) throws InvalidInputException, UserAlreadyExistsException {
        User user = new User();
        if (userRepository.findDTOByUsername(registerRequest.getUsername()).isPresent())
            throw new UserAlreadyExistsException();
        if (!Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", registerRequest.getEmail()))
            throw new InvalidInputException("Email is not valid");
        if (registerRequest.getUsername().trim().isEmpty()) throw new InvalidInputException("Username cannot be empty");
        if (registerRequest.getPassword().length() < 8)
            throw new InvalidInputException("Password must be bigger than 8");
        user.setUsername(registerRequest.getUsername());
        user.setPassword(ShaUtils.decode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.USER);
        return userDTOMapper.apply(userRepository.save(user));
    }

    
    public CompanyDTO registerCompany(RegisterRequest registerRequest) throws InvalidInputException, CompanyAlreadyExistsException {
        Company company = new Company();
        if (companyRepository.findByName(registerRequest.getUsername()).isPresent())
            throw new CompanyAlreadyExistsException();
        if (!Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", registerRequest.getEmail()))
            throw new InvalidInputException("Email is not valid");
        if (registerRequest.getUsername().trim().isEmpty()) throw new InvalidInputException("Username cannot be empty");
        if (registerRequest.getPassword().length() <= 8)
            throw new InvalidInputException("Password must be bigger than 8");
        company.setName(registerRequest.getUsername());
        company.setEmail(registerRequest.getEmail());
        company.setPassword(ShaUtils.decode(registerRequest.getPassword()));
        return companyDTOMapper.apply(companyRepository.save(company));
    }

    public Map<String, Boolean> checkLogin(String username) {
        return Map.of("loggedIn", userRepository.existsByUsername(username));
    }

    public VerifyDto verifyToken(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        return user.map(current -> new VerifyDto(true, userDTOMapper.apply(current))).orElseGet(() -> new VerifyDto(false, null));
    }
}
