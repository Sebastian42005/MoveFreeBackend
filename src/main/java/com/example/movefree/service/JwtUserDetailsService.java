package com.example.movefree.service;

import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()).build();
    }

    public UserDetails verifyUser(String username, String password) throws UsernameNotFoundException {
        UserDTO user = userRepository.login(username, password).orElseThrow(() -> new UsernameNotFoundException("Wrong login credentials"));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()).build();
    }
}
