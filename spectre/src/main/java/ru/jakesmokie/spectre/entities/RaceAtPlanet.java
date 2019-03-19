package ru.jakesmokie.spectre.entities;

import lombok.Data;
import ru.jakesmokie.spectre.providers.gson.SkipSerialisation;

import javax.persistence.*;

@Entity
@Data
public class RaceAtPlanet {
    @EmbeddedId
    private RaceAtPlanetKey id;
    private int dangerLevel;

    public RaceAtPlanet(int dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public RaceAtPlanet() {
    }
}
