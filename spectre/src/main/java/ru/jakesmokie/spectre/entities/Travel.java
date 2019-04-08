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

    @ManyToOne
    private Tourist tourist;

    @ManyToOne
    private Station origin;

    @ManyToOne
    private Station destination;

    private Date date;

    public Travel() {
    }

    public Travel(Tourist tourist, Station origin, Station destination, Date date) {
        this.tourist = tourist;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }
}
