package com.example.movefree.controller.company.company;

import com.example.movefree.Tokens;
import com.example.movefree.database.company.requests.CompanyRequestDTO;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.role.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
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
class CompanyControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void editCompany() throws Exception {
        PostSpotRequestBody.CompanyEditRequestBody requestBody = new PostSpotRequestBody.CompanyEditRequestBody("randomAddress", "+123456789");
        this.mockMvc.perform(put("/api/company/edit")
                        .header("Authorization", "Bearer " + Tokens.COMPANY_TOKEN)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType("application/json")
                        .with(user("Company").roles(Role.COMPANY)))
                .andExpect(status().isOk());
    }

    @Test
    void requestCompany_and_deleteRequest() throws Exception{
        CompanyRequestDTO.Request requestBody = new CompanyRequestDTO.Request();
        requestBody.setMessage("Become Company Test");
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/company/request")
                        .with(user("Sebastian").roles(Role.USER))
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType("application/json"))
                .andReturn().getResponse();

        CompanyRequestDTO requestDTO = objectMapper.readValue(response.getContentAsString(), CompanyRequestDTO.class);
        Assertions.assertEquals(200, response.getStatus());

        this.mockMvc.perform(delete("/api/admin/company-requests/" + requestDTO.getId() + "/decline")
                        .header("Authorization", "Bearer " + Tokens.ADMIN_TOKEN))
                .andExpect(status().is(200));
    }
}
