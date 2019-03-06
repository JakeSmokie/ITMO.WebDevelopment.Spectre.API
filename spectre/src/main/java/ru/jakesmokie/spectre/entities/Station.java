package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Station {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Planet planet;

    private String name;
    private String description;

    public Station() {
    }
}
