package ru.jakesmokie.spectre.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Story {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Tourist author;

    private String text;
    private Date submitionDate;
    private Boolean isAccepted;

    public Story() {
    }
}
