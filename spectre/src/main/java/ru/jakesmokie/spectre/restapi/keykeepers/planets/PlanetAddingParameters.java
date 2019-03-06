package ru.jakesmokie.spectre.restapi.keykeepers.planets;

import com.google.gson.Gson;
import lombok.Data;
import lombok.val;
import ru.jakesmokie.spectre.entities.Planet;

import java.util.Date;

@Data
public class PlanetAddingParameters {
    private static final Gson gson = new Gson();

    private final String token;
    private final String name;
    private final String description;
    private final Date birthDate;

    public static PlanetAddingParameters fromJson(String json) {
        return gson.fromJson(json, PlanetAddingParameters.class);
    }

    public static String toJson(PlanetAddingParameters response) {
        return gson.toJson(response);
    }

    public Planet toPlanet() {
        val planet = new Planet();
        planet.setName(name);
        planet.setDescription(description);
        planet.setBirthDate(birthDate);
        planet.setCreationDate(new Date());

        return planet;
    }
}
