package ru.jakesmokie.spectre.entities;

import lombok.Data;
import ru.jakesmokie.spectre.providers.gson.SkipSerialisation;

import javax.persistence.*;

@Entity
@Data
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(name = "planet_id", updatable = false, insertable = false)
    private int planetId;

    @ManyToOne
    @SkipSerialisation
    private Planet planet;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }
}
