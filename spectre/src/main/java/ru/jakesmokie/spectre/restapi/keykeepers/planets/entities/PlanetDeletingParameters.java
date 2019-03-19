package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class PlanetDeletingParameters {
    private String token;
    private int planetId;

    public PlanetDeletingParameters(String token, int planetId) {
        this.token = token;
        this.planetId = planetId;
    }

    public PlanetDeletingParameters() {
    }
}
