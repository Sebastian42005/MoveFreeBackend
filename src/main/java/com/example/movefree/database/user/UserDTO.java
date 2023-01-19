package com.example.movefree.database.user;

import com.example.movefree.database.company.company.CompanyDTO;
import com.example.movefree.database.spot.rating.RatingDTO;
import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.spot.spot.SpotDTOResponse;
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;
    private String role;

    @ManyToMany
    private List<UserDTO> follows;

    @ManyToMany(mappedBy= "follows")
    private List<UserDTO> follower;

    @JsonManagedReference("user_spot")
    @OneToMany(mappedBy = "user")
    private List<SpotDTO> spots;

    @ManyToMany(mappedBy = "savedBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SpotDTO> savedSpots = new ArrayList<>();

    private byte[] profilePicture;
    private String contentType;

    @JsonManagedReference("user_ratings")
    @OneToMany(mappedBy = "user")
    List<RatingDTO> ratings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_company", referencedColumnName = "id")
    private CompanyDTO company;
}
