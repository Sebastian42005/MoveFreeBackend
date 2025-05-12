package com.example.movefree.database.user;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.sport.Sport;
import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;
    private String description = "";
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    private List<User> follows;

    @ManyToMany(mappedBy= "follows")
    private List<User> follower;

    @ManyToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Company> followCompanies = new ArrayList<>();


    @JsonManagedReference("user_spot")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Spot> spots;

    @ManyToMany(mappedBy = "savedBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Spot> savedSpots = new ArrayList<>();

    @JsonIgnore
    private byte[] profilePicture;
    @JsonIgnore
    private String contentType;

    @JsonManagedReference("user_ratings")
    @OneToMany(mappedBy = "user")
    List<Rating> ratings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorite_sports",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_name")
    )
    private List<Sport> favoriteSports = new ArrayList<>();
}
