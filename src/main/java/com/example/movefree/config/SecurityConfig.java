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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.example.movefree")
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String allSpots = "/api/spot/**";
        return httpSecurity
                .cors().disable().csrf().disable()
                .authorizeRequests(auth -> {
                    auth.antMatchers("/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/v2/api-docs/**",
                            "/swagger-resources/**").permitAll();
                    //Authentication
                    auth.antMatchers("/api/authentication/**").permitAll();
                    //Only for Admins
                    auth.antMatchers("/api/admin/**").hasRole(Role.ADMIN);
                    //User -> Company
                    auth.antMatchers("/api/company/request").hasRole(Role.USER);
                    //Only for Companies
                    auth.antMatchers(HttpMethod.GET, "/api/company/**").permitAll();
                    auth.antMatchers("/api/company/**").hasRole(Role.COMPANY);
                    //Security for Spot requests
                    auth.antMatchers(HttpMethod.POST, allSpots).hasRole(Role.USER);
                    auth.antMatchers(HttpMethod.PUT, allSpots).hasRole(Role.USER);
                    auth.antMatchers(allSpots).permitAll();
                    auth.antMatchers("/api/user/profile").hasRole(Role.USER);
                    auth.antMatchers("/api/user/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint).and()
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
