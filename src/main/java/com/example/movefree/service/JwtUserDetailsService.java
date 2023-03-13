package com.example.movefree.service;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.company.company.CompanyRepository;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.exception.WrongLoginCredentialsException;
import com.example.movefree.role.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    final CompanyRepository companyRepository;

    public JwtUserDetailsService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.example.movefree.database.user.User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return User.withUsername(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(user.get().getRole()).build();
        } else {
            Optional<Company> company = companyRepository.findByName(username);
            if (company.isPresent()) {
                return User.withUsername(company.get().getName())
                        .password(company.get().getPassword())
                        .roles(Role.COMPANY).build();
            }else {
                throw new UsernameNotFoundException("User/Company not found with username: " + username);
            }
        }
    }

    public UserDetails verifyUser(String username, String password) throws WrongLoginCredentialsException {
        Optional<com.example.movefree.database.user.User> user = userRepository.login(username, password);
        if (user.isPresent()) {
            return User.withUsername(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(user.get().getRole()).build();
        } else {
            Optional<Company> company = companyRepository.login(username, password);
            if (company.isPresent()) {
                return User.withUsername(company.get().getName())
                        .password(company.get().getPassword())
                        .roles(Role.COMPANY).build();
            }else {
                throw new WrongLoginCredentialsException();
            }
        }
    }
}
