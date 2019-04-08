package ru.jakesmokie.spectre.entities;

import com.google.gson.JsonObject;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Tourist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private boolean canSendStory;

    @Column(nullable = false)
    private boolean canUseGates;

    @Column(nullable = false)
    private boolean disabled;

    @Column(unique = true)
    private String name;

    @ManyToOne
    private Race race;

    @ManyToOne
    private Station origin;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Planet currentPlanet;

    @Column(nullable = false)
    private int score;

    @Transient
    private JsonObject props;

    public Tourist() {
    }

    public Tourist(String name, Race race, Station origin) {
        this.name = name;
        this.race = race;
        this.origin = origin;
    }
}
