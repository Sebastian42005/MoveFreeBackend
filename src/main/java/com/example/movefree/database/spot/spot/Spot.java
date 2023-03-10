package com.example.movefree.database.spot.spot;

import com.example.movefree.database.spot.image.SpotPicture;
import com.example.movefree.database.spot.location.Location;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.spottype.SpotType;
import com.example.movefree.database.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "spots")
public class Spot {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String description;

    @JsonManagedReference("spot_rating")
    @OneToMany(mappedBy = "spot")
    private List<Rating> ratings;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    SpotType spotType;

    @JsonBackReference("user_spot")
    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_saved_spots",
            joinColumns = { @JoinColumn(name = "spot_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
    @JsonIgnore
    private List<User> savedBy = new ArrayList<>();

    @JsonManagedReference("spot_pictures")
    @OneToMany(mappedBy = "spot")
    private List<SpotPicture> pictures;
}
