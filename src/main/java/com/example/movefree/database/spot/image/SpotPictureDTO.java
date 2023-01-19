package com.example.movefree.database.spot.image;

import com.example.movefree.database.spot.spot.SpotDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "spot_images")
public class SpotPictureDTO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte[] picture;

    private String contentType;

    @JsonBackReference("spot_pictures")
    @ManyToOne
    private SpotDTO spot;
}
