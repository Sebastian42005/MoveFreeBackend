package com.example.movefree.database.spot.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "locations")
public class LocationDTO {

    @Id
    @GeneratedValue
    private int id;

    private double latitude;
    private double longitude;
    private String city;

}
