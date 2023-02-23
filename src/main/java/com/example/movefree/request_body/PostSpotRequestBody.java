package com.example.movefree.request_body;

import com.example.movefree.database.spot.spottype.SpotType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostSpotRequestBody {
    private String description;
    private Double latitude;
    private Double longitude;
    private String city;
    private SpotType spotType;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyEditRequestBody {
        private String address;
        private String phoneNumber;
    }
}
