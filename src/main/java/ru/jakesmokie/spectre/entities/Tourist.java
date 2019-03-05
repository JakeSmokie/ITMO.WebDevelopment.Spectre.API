package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Tourist {
    @Id
    @GeneratedValue
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
