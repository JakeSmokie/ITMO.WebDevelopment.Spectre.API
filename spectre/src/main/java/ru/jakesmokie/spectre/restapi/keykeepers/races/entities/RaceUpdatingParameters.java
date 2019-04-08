package ru.jakesmokie.spectre.restapi.keykeepers.races.entities;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class RaceUpdatingParameters {
    private int id;
    private String name;
    private String token;

    public RaceUpdatingParameters() {
    }
}
