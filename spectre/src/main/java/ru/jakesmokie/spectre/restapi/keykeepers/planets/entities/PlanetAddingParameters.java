package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;
import lombok.val;
import ru.jakesmokie.spectre.entities.Planet;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class PlanetAddingParameters {
    private String token;
    private String name;
    private String description;

    public PlanetAddingParameters(String token, String name, String description) {
        this.token = token;
        this.name = name;
        this.description = description;
    }

    public PlanetAddingParameters() {
    }

    public Planet toPlanet() {
        val planet = new Planet();
        planet.setName(name);
        planet.setDescription(description);

        return planet;
    }
}
