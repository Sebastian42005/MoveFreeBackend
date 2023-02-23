package com.example.movefree.database.user;

import com.example.movefree.database.company.company.Company;
import com.example.movefree.database.spot.rating.Rating;
import com.example.movefree.database.spot.spot.Spot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;
    private String role;

    @ManyToMany
    private List<User> follows;

    @ManyToMany(mappedBy= "follows")
    private List<User> follower;

    @JsonManagedReference("user_spot")
    @OneToMany(mappedBy = "user")
    private List<Spot> spots;

    @ManyToMany(mappedBy = "savedBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Spot> savedSpots = new ArrayList<>();

    private byte[] profilePicture;
    private String contentType;

    @JsonManagedReference("user_ratings")
    @OneToMany(mappedBy = "user")
    List<Rating> ratings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_company", referencedColumnName = "id")
    private Company company;
}
