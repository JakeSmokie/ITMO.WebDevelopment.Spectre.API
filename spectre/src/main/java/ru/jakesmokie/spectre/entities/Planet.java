package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@XmlRootElement
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(length = 10000)
    private String description;
    private boolean disabled;

    @OneToMany
    private List<RaceAtPlanet> races = new ArrayList<>();

    @OneToMany
    private List<Station> stations = new ArrayList<>();

    public Planet() {
    }

    public Planet(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
