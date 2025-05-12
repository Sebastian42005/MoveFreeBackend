package com.example.movefree.request_body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostSpotRequestBody {
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String city;
    private List<String> attributes = new ArrayList<>();
    private String sport;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyEditRequestBody {
        private String address;
        private String phoneNumber;
    }
}
