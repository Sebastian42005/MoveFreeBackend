package com.example.movefree.database.spot.image;

import com.example.movefree.database.spot.spot.Spot;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "spot_images")
public class SpotPicture {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private byte[] picture;

    private String contentType;

    @JsonBackReference("spot_pictures")
    @ManyToOne
    private Spot spot;
}
