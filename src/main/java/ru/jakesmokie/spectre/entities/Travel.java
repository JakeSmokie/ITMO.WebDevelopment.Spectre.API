package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class Travel {
    @Id
    @GeneratedValue
    private int id;
    // TODO: Fields

    @ManyToOne
    private Tourist tourist;

    @ManyToOne
    private Station origin;

    @ManyToOne
    private Station destination;

    private Date date;

    public Travel() {
    }
}
