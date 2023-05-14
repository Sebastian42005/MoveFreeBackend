package com.example.movefree.config;

import com.example.movefree.role.Role;
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

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(CustomAuthenticationEntryPoint authenticationEntryPoint, JwtRequestFilter jwtRequestFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String allSpots = "/api/spot/**";
        return httpSecurity
                .cors().and().csrf().disable()
                .authorizeRequests(auth -> {
                    //Spot Types
                    auth.antMatchers(HttpMethod.POST, "/api/spot/type/**").hasRole(Role.ADMIN);
                    auth.antMatchers(HttpMethod.DELETE, "/api/spot/type/**").hasRole(Role.ADMIN);
                    auth.antMatchers(HttpMethod.GET, "/api/spot/type").permitAll();
                    //Authentication
                    auth.antMatchers("/api/authentication/**").permitAll();
                    //Only for Admins
                    auth.antMatchers("/api/admin/**").hasRole(Role.ADMIN);
                    //User -> Company
                    auth.antMatchers("/api/company/request").hasRole(Role.USER);
                    //Only for Companies
                    auth.antMatchers(HttpMethod.GET, "/api/company/**").permitAll();
                    auth.antMatchers("/api/company/**").hasRole(Role.COMPANY);
                    // Spot
                    auth.antMatchers(HttpMethod.POST, allSpots).hasRole(Role.USER);
                    auth.antMatchers(HttpMethod.PUT, allSpots).hasRole(Role.USER);
                    auth.antMatchers(allSpots).permitAll();
                    //Get Own profile as user
                    auth.antMatchers("/api/user/own/*").hasRole(Role.USER);
                    auth.antMatchers("/api/user/own").hasAnyRole(Role.USER, Role.COMPANY, Role.ADMIN);
                    //Request to get User
                    auth.antMatchers("/api/user/**").permitAll();
                    //Swagger
                    auth.antMatchers("/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/v2/api-docs/**",
                            "/swagger-resources/**").permitAll();
                    auth.antMatchers("/api/authentication/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint).and()
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
