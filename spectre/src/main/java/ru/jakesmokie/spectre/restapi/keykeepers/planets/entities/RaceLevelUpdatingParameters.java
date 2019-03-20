package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class RaceLevelUpdatingParameters {
    private String token;
    private int planet;
    private int race;
    private int level;

    public RaceLevelUpdatingParameters() {
    }
}
