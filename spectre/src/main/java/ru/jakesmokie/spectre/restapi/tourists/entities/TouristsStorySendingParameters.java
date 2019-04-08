package ru.jakesmokie.spectre.restapi.tourists.entities;

import lombok.Data;

@Data
public class TouristsStorySendingParameters {
    private String token;
    private String name;
    private String text;

    public TouristsStorySendingParameters() {
    }
}
