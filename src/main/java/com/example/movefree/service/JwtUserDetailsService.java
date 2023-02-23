package com.example.movefree.service;

import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.WrongLoginCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.movefree.database.user.User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()).build();
    }

    public UserDetails verifyUser(String username, String password) throws WrongLoginCredentialsException {
        com.example.movefree.database.user.User user = userRepository.login(username, password).orElseThrow(WrongLoginCredentialsException::new);
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()).build();
    }
}
