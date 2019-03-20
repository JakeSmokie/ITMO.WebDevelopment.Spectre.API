package ru.jakesmokie.spectre.restapi.keykeepers.races.entities;

import lombok.Data;
import ru.jakesmokie.spectre.entities.Planet;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement
public class RaceUpdatingParameters {
    private int id;
    private String name;
    private String token;

    public RaceUpdatingParameters() {
    }
}
