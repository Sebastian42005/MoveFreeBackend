package com.example.movefree.request_body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateSpotRequestBody {
    @Min(1)
    @Max(5)
    private int stars;
    private String message;
}
