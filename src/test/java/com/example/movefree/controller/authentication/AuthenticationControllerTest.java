package com.example.movefree.controller.authentication;

import com.example.movefree.config.JwtTokenUtil;
import com.example.movefree.database.user.UserDTO;
import com.example.movefree.database.user.UserRepository;
import com.example.movefree.role.Role;
import com.example.movefree.service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5431/postgres",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.password=1234",
        "spring.datasource.username=postgres"
})
class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private UserDTO requestBody = new UserDTO();

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        requestBody.setUsername(UUID.randomUUID().toString());
        requestBody.setPassword("test1234");
        requestBody.setEmail("email@test.at");
    }

    @Test
    void loginAndRegister() throws Exception {

        this.mockMvc.perform(post("/authentication/register")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType("application/json"))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/authentication/login")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
