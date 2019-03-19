package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Tourist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Race race;

    @ManyToOne
    private Planet origin;

    private String name;
    private String bio;

    public Tourist() {
    }
}
