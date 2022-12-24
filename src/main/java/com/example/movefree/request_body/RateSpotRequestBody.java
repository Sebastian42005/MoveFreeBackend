package com.example.movefree.request_body;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class RateSpotRequestBody {
    @Min(1)
    @Max(5)
    private int stars;
    private String message;
}
