package ru.jakesmokie.spectre.entities;

import lombok.Data;
import ru.jakesmokie.spectre.providers.gson.SkipSerialisation;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(length = 10000)
    private String description;

    @SkipSerialisation
    @OneToMany(mappedBy = "race")
    private List<RaceAtPlanet> planets;

    public Race() {
    }

    public Race(String name) {
        this.name = name;
    }
}
