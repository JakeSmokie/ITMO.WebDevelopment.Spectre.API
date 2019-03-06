package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Race {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String description;

    public Race() {
    }
}
