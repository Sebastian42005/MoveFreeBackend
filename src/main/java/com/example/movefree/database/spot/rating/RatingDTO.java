package com.example.movefree.database.spot.rating;

import com.example.movefree.database.spot.spot.SpotDTO;
import com.example.movefree.database.user.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ratings")
public class RatingDTO {

    public RatingDTO(String message, UserDTO user, SpotDTO spot, int stars) {
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
    UserDTO user;

    @JsonBackReference("spot_rating")
    @ManyToOne
    private SpotDTO spot;

    @Max(5)
    @Min(1)
    private int stars;
}
