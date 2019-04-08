package ru.jakesmokie.spectre.restapi.keykeepers.tourists.entities;

import lombok.Data;

@Data
public class StoryRatingParameters {
    private String token;
    private int id;
    private int score;

    public StoryRatingParameters() {
    }
}
