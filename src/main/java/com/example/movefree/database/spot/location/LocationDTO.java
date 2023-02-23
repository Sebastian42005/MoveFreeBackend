package com.example.movefree.database.spot.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
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
