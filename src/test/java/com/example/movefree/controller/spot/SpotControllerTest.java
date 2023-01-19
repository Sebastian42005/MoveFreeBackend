package com.example.movefree.controller.spot;

import com.example.movefree.Tokens;
import com.example.movefree.database.spot.spot.SpotDTOResponse;
import com.example.movefree.database.spot.spotType.SpotType;
import com.example.movefree.request_body.PostSpotRequestBody;
import com.example.movefree.request_body.RateSpotRequestBody;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5431/postgres",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.password=1234",
        "spring.datasource.username=postgres"
})
class SpotControllerTest {

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;

    PostSpotRequestBody spotRequestBody = new PostSpotRequestBody();

    RateSpotRequestBody rateSpotRequestBody = new RateSpotRequestBody(3, "Sick");

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        spotRequestBody.setSpotType(SpotType.parkour);
        spotRequestBody.setDescription("Description");
        spotRequestBody.setCity("Vienna");
        spotRequestBody.setLongitude(0.0);
        spotRequestBody.setLatitude(0.0);
    }

    @Test
    void getRatings() throws Exception {
        this.mockMvc.perform(get("/api/spot/1/ratings")).andExpect(status().isOk());
    }

    @Test
    void postSpot_asUser() throws Exception {
        this.mockMvc.perform(post("/api/spot/post")
                        .content(objectMapper.writeValueAsString(spotRequestBody))
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + Tokens.USER_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void postSpot_noUser() throws Exception {
        this.mockMvc.perform(post("/api/spot/post")
                        .content(objectMapper.writeValueAsString(spotRequestBody)))
                .andExpect(status().is(403));
    }

    @Test
    void postSpot_asCompany() throws Exception {
        this.mockMvc.perform(post("/api/spot/post")
                        .content(objectMapper.writeValueAsString(spotRequestBody))
                        .header("Authorization", "Bearer " + Tokens.COMPANY_TOKEN))
                .andExpect(status().is(403));
    }

    @Test
    void rateSpot_asUser() throws Exception {
        postSpot_asUser();
        this.mockMvc.perform(put("/api/spot/1/rate")
                        .content(objectMapper.writeValueAsString(rateSpotRequestBody))
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + Tokens.USER_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void rateSpot_noUser() throws Exception {
        this.mockMvc.perform(put("/api/spot/1/rate")
                        .content(objectMapper.writeValueAsString(rateSpotRequestBody)))
                .andExpect(status().is(403));
    }

    @Test
    void rateSpot_asCompany() throws Exception {
        this.mockMvc.perform(put("/api/spot/1/rate")
                        .content(objectMapper.writeValueAsString(rateSpotRequestBody))
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + Tokens.COMPANY_TOKEN))
                .andExpect(status().is(403));
    }

    @Test
    void searchSpot_limit() throws Exception {
        List list = objectMapper.readValue(this.mockMvc.perform(get("/api/spot/all?limit=5")).andReturn().getResponse().getContentAsString(), List.class);
        Assertions.assertTrue(list.size() <= 5);
    }

    @Test
    void searchSpot_onlyParkour() throws Exception {
        List<SpotDTOResponse> list = objectMapper.readValue(this.mockMvc.perform(get("/api/spot/all?spotType=parkour"))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertTrue(list.stream().allMatch(current -> current.getSpotType() == SpotType.parkour));
    }
    @Test
    void searchSpot_onlyFromVienna() throws Exception {
        List<SpotDTOResponse> list = objectMapper.readValue(this.mockMvc.perform(get("/api/spot/all?city=vienna"))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertTrue(list.stream().allMatch(current -> current.getLocation().getCity().equalsIgnoreCase("vienna")));
    }

    @Test
    void searchSpot_noParams() throws Exception {
        this.mockMvc.perform(get("/api/spot/all")).andExpect(status().isOk());
    }
}
