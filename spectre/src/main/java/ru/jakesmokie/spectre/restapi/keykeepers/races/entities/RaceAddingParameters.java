package ru.jakesmokie.spectre.restapi.keykeepers.races.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class RaceAddingParameters {
    private String token;

    public RaceAddingParameters() {
    }
}
