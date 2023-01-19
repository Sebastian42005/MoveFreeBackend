package com.example.movefree.database.user;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.company.company.CompanyDTOResponse;
import com.example.movefree.database.spot.spot.SpotDTOResponse;
import com.example.movefree.role.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTOResponse {
    private String username;
    private String role;
    private List<SpotDTOResponse> spots;
    private int follows;
    private int follower;
    private CompanyDTOResponse companyDTO;

    public UserDTOResponse(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.role = userDTO.getRole();
        this.spots = userDTO.getSpots().stream().map(SpotDTOResponse::new).toList();
        this.follows = userDTO.getFollows().size();
        this.follower = userDTO.getFollower().size();
        if (userDTO.getRole() != null) {
            if (userDTO.getRole().equals(Role.COMPANY)) {
                this.companyDTO = new CompanyDTOResponse(userDTO.getCompany());
            }
        }
    }
}
