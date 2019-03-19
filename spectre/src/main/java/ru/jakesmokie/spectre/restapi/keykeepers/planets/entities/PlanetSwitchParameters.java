package ru.jakesmokie.spectre.restapi.keykeepers.planets.entities;

import lombok.Data;
import ru.jakesmokie.spectre.entities.Planet;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement
public class PlanetSwitchParameters {
    private Integer id;
    private String token;

    public PlanetSwitchParameters() {
    }
}
