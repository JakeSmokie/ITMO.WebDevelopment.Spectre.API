package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;
import ru.jakesmokie.spectre.entities.Planet;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement
public class PlanetUpdatingParameters {
    private Integer id;
    private String token;
    private String name;
    private String description;

    public PlanetUpdatingParameters(String token, String name, String description, Date birthDate) {
        this.token = token;
        this.name = name;
        this.description = description;
    }

    public PlanetUpdatingParameters() {
    }

    public void copyToPlanet(Planet planet) {
        planet.setId(id);
        planet.setName(name);
        planet.setDescription(description);
    }
}
