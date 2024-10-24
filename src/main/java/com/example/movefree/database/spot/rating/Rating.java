package com.example.movefree.database.spot.rating;

import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ratings")
public class Rating {

    public Rating(String message, User user, Spot spot, int stars) {
        this.message = message;
        this.user = user;
        this.spot = spot;
        this.stars = stars;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;

    @JsonBackReference("user_ratings")
    @ManyToOne
    User user;

    @JsonBackReference("spot_rating")
    @ManyToOne
    private Spot spot;

    @Max(5)
    @Min(1)
    private int stars;
}
