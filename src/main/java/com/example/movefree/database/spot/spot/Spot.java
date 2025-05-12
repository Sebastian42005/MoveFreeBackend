package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.sport.Sport;
import com.example.movefree.database.spot.image.SpotPicture;
import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.spottype.Attribute;
import com.example.movefree.database.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "spots")
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    @JsonManagedReference("spot_rating")
    @OneToMany(mappedBy = "spot")
    private List<Rating> ratings;

    private Instant createdAt;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "spot_attributes",
            joinColumns = { @JoinColumn(name = "spot_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "attribute_id", referencedColumnName = "id") })
    @JsonIgnore
    List<Attribute> attributes;

    @JsonBackReference("user_spot")
    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_saved_spots",
            joinColumns = { @JoinColumn(name = "spot_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
    @JsonIgnore
    private List<User> savedBy = new ArrayList<>();

    @OneToMany(mappedBy = "spot")
    @JsonIgnore
    private List<SpotPicture> pictures;

    @ManyToOne
    private Sport sport;
}
