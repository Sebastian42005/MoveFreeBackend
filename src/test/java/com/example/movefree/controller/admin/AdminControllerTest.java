package com.example.movefree.controller.admin;

import com.example.movefree.Tokens;
import com.example.movefree.role.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5431/postgres",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.password=1234",
        "spring.datasource.username=postgres"
})
class AdminControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    void getAllRequests() throws Exception {
        this.mockMvc.perform(get("/admin/company-requests")
                        .with(user("Admin").roles(Role.ADMIN)))
                .andExpect(status().is(200));
    }

    @Test
    void declineRequest() throws Exception {
        this.mockMvc.perform(delete("/admin/company-requests/999/decline")
                        .header("Authorization", "Bearer " + Tokens.adminToken))
                .andExpect(status().is(404));
    }

    @Test
    void acceptRequest() throws Exception {
        this.mockMvc.perform(patch("/admin/company-requests/999/accept")
                        .header("Authorization", "Bearer " + Tokens.adminToken))
                .andExpect(status().is(404));
    }

    @Test
    void getAllRequests_asUser() throws Exception {
        this.mockMvc.perform(get("/admin/company-requests")
                        .with(user("Sebastian").roles(Role.USER)))
                .andExpect(status().is(403));
    }

    @Test
    void getAllRequests_noUser() throws Exception {
        this.mockMvc.perform(get("/admin/company-requests"))
                .andExpect(status().is(401));
    }
}
