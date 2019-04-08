package ru.jakesmokie.spectre.restapi.keykeepers.tourists.entities;

import lombok.Data;

@Data
public class TouristSwitchingParameters {
    private String token;
    private int id;

    public TouristSwitchingParameters() {
    }
}
