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

    @ManyToOne
    @SkipSerialisation
    @MapsId
    @JoinColumn(name = "planet")
    private Planet planet;

    @ManyToOne
    @SkipSerialisation
    @MapsId
    @JoinColumn(name = "race")
    private Race race;

    public RaceAtPlanet() {
    }

    public RaceAtPlanet(RaceAtPlanetKey id, int dangerLevel, Planet planet, Race race) {
        this.id = id;
        this.dangerLevel = dangerLevel;
        this.planet = planet;
        this.race = race;
    }
}
