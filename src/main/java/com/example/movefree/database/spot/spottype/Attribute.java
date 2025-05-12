package com.example.movefree.database.spot.spottype;

import com.example.movefree.database.spot.sport.Sport;
import com.example.movefree.database.spot.spot.Spot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "attributes")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @JsonIgnore
    private byte[] image;

    @Column(length = 7)
    private String color;

    @JsonIgnore
    private String contentType;

    @ManyToOne
    @JsonIgnore
    Sport sport;

    @ManyToMany(mappedBy = "attributes", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Spot> spots;
}

