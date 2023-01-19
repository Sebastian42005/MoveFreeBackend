package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.image.SpotPictureDTO;
import com.example.movefree.database.spot.location.LocationDTO;
import com.example.movefree.database.spot.spotType.SpotType;
import com.example.movefree.database.user.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpotDTOResponse {
    private int id;
    private String description;
    private LocationDTO location;
    private SpotType spotType;
    private String user;

    private List<Integer> pictures;

    public SpotDTOResponse(SpotDTO spotDTO) {
        this.id = spotDTO.getId();
        this.description = spotDTO.getDescription();
        this.location = spotDTO.getLocation();
        this.spotType = spotDTO.getSpotType();
        this.user = spotDTO.getUser().getUsername();
        this.pictures = spotDTO.getPictures().stream().map(SpotPictureDTO::getId).toList();
    }
}
