package ru.jakesmokie.spectre.restapi.tourists.entities;

import lombok.Data;

@Data
public class GatesUsingParameters {
    private String token;
    private int origin;
    private int destination;

    public GatesUsingParameters() {
    }
}
