package com.example.movefree.database.spot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "sport")
public class Sport {
    @Id
    private String name;

    @JsonIgnore
    private byte[] image;

    @JsonIgnore
    private String contentType;
}
