package com.example.movefree.database.spot.rating;

import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
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
    private int id;

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
