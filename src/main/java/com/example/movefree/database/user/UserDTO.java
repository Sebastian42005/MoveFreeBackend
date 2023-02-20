package com.example.movefree.database.user;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.spot.spot.SpotDTO;

import java.util.List;

public record UserDTO (
        int id,
        String username,
        String role,
        List<SpotDTO> spots,
        int follower,
        int follows,
        CompanyDTO companyDTO
) {
}
