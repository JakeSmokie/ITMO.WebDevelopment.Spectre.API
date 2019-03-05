package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Data
@Entity
public class Planet {
    @Id
    @GeneratedValue
    private int id;


    private String name;
    private String description;
    private Date birthDate;
    private Date creationDate;

    public Planet() {
    }
}
