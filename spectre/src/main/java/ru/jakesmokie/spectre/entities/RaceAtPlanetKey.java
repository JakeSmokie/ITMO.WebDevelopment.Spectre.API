package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class RaceAtPlanetKey implements Serializable {
    private int race;
    private int planet;

    public RaceAtPlanetKey() {
    }

    public RaceAtPlanetKey(int race, int planet) {
        this.race = race;
        this.planet = planet;
    }
}
