package com.example.movefree.config;

import com.example.movefree.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.example.movefree")
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String allSpots = "/spot/**";
        return httpSecurity
                .csrf().disable()
                .authorizeRequests(auth -> {
                    //Authentication
                    auth.antMatchers("/authentication/**").permitAll();
                    //Only for Admins
                    auth.antMatchers("/admin/**").hasRole(Role.ADMIN);
                    //User -> Company
                    auth.antMatchers("/company/request").hasRole(Role.USER);
                    //Only for Companies
                    auth.antMatchers(HttpMethod.GET, "/company/*").permitAll();
                    auth.antMatchers("/company/**").hasRole(Role.COMPANY);
                    //Security for Spot requests
                    auth.antMatchers(HttpMethod.POST, allSpots).hasRole(Role.USER);
                    auth.antMatchers(HttpMethod.PUT, allSpots).hasRole(Role.USER);
                    auth.antMatchers(allSpots).permitAll();
                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
