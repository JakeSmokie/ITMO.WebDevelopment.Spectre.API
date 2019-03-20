package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class StationUpdatingParameters {
    private String token;
    private int station;
    private String name;

    public StationUpdatingParameters() {
    }
}
