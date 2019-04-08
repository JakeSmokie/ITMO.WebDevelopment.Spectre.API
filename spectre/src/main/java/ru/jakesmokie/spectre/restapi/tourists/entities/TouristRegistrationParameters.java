package ru.jakesmokie.spectre.restapi.tourists.entities;

import lombok.Data;

@Data
public class TouristRegistrationParameters {
    private String token;
    private int race;
    private int planet;
    private int station;

    public TouristRegistrationParameters() {
    }
}
