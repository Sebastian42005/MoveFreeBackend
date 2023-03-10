package com.example.movefree.database.user;

import com.example.movefree.database.company.company.CompanyDTOMapper;
import com.example.movefree.database.spot.spot.SpotDTOMapper;

import java.util.function.Function;

public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getRole(),
                user.getSpots().stream().map(new SpotDTOMapper()).toList(),
                user.getFollower().size(),
                user.getFollows().size(),
                new CompanyDTOMapper().apply(user.getCompany())
        );
    }
}
