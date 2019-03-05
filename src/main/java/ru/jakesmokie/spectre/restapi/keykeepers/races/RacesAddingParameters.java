package ru.jakesmokie.spectre.restapi.keykeepers.races;

import com.google.gson.Gson;
import lombok.Data;

import java.util.Date;

@Data
public class RacesAddingParameters {
    private static final Gson gson = new Gson();

    private final String token;
    private final String name;
    private final String description;
    private final Date birthDate;

    public static RacesAddingParameters fromJson(String json) {
        return gson.fromJson(json, RacesAddingParameters.class);
    }

    public static String toJson(RacesAddingParameters response) {
        return gson.toJson(response);
    }
}
