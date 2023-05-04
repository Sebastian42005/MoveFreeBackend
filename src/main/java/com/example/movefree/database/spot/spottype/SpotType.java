package com.example.movefree.database.spot.spottype;

import com.example.movefree.database.spot.spot.Spot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "spot_types")
public class SpotType {

    @Id
    private String name;

    private byte[] image;

    private String contentType;

    @ManyToMany(mappedBy = "spotTypes", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Spot> spots;
}

