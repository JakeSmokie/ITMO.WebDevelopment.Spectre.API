package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class StationAddingParameters {
    private Integer id;
    private String token;

    public StationAddingParameters() {
    }
}
