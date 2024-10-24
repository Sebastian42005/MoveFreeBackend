package com.example.movefree.config;

import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.role.Role;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@ComponentScan("com.example.movefree")
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final UserRepository userRepository;

    @Bean
    public User setUpAdminUser() {
        return userRepository.findByUsername("administrator").orElseGet(() ->
                userRepository.save(
                        User.builder()
                                .email("administrator@gmail.com")
                                .description("Admin of the Application")
                                .username("administrator")
                                .password(ShaUtils.decode("tollesadminpasswort"))
                                .role(Role.ADMIN)
                                .build()));
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
                    auth.antMatchers(HttpMethod.POST, "/api/spot/type/**").hasRole(Role.ADMIN.getName());
                    auth.antMatchers(HttpMethod.DELETE, "/api/spot/type/**").hasRole(Role.ADMIN.getName());
                    auth.antMatchers(HttpMethod.GET, "/api/spot/type").permitAll();
                    //Authentication
                    auth.antMatchers("/api/authentication/**").permitAll();
                    //Only for Admins
                    auth.antMatchers("/api/admin/**").hasRole(Role.ADMIN.getName());
                    //User -> Company
                    auth.antMatchers("/api/company/request").hasRole(Role.USER.getName());
                    //Only for Companies
                    auth.antMatchers(HttpMethod.GET, "/api/company/**").permitAll();
                    auth.antMatchers("/api/company/**").hasRole(Role.COMPANY.getName());
                    // Spot
                    auth.antMatchers("/api/spot/saved").authenticated();
                    auth.antMatchers(HttpMethod.POST, allSpots).authenticated();
                    auth.antMatchers(HttpMethod.PUT, allSpots).authenticated();
                    auth.antMatchers(allSpots).permitAll();
                    //Get Own profile as
                    auth.antMatchers("/api/user/own/*").authenticated();
                    auth.antMatchers("/api/user/own").authenticated();
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
