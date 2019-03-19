package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
