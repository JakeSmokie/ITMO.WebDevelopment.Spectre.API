package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class RaceAtPlanet {
    @Id
    @ManyToOne
    private Race race;

    @Id
    @ManyToOne
    private Planet planet;

    @ManyToOne
    private DangerLevel dangerLevel;


    public RaceAtPlanet() {
    }
}
