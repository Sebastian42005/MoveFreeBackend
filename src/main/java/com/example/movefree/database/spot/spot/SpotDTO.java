package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.location.LocationDTO;
import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.spotType.SpotType;
import com.example.movefree.database.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "spots")
public class SpotDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @JsonManagedReference("spot_rating")
    @OneToMany(mappedBy = "spot")
    private List<RatingDTO> ratings;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private LocationDTO location;

    SpotType spotType;

    @JsonBackReference("user_spot")
    @ManyToOne
    private UserDTO user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_saved_spots",
            joinColumns = { @JoinColumn(name = "spot_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
    @JsonIgnore
    private List<SpotDTO> savedBy = new ArrayList<>();

}
