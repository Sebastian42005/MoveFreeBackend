package com.example.movefree.database.spot.sport;

import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.database.spot.spottype.Attribute;
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
@Table(name = "sport")
public class Sport {
    @Id
    private String name;

    private String symbol;
    @Column(length = 7)
    private String color;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "sport")
    List<Attribute> attributes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "sport")
    @JsonIgnore
    List<Spot> spots;

    public Sport(String name) {
        this.name = name;
    }
}
