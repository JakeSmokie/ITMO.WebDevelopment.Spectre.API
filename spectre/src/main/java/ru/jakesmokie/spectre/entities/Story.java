package ru.jakesmokie.spectre.entities;

import lombok.Data;
import ru.jakesmokie.spectre.providers.gson.SkipSerialisation;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "author_id", updatable = false, insertable = false)
    private int authorId;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @SkipSerialisation
    private Tourist author;

    @Column(length = 1000000)
    private String text;

    private String name;
    private Date submissionDate;
    private boolean isAccepted;
    private int score;

    public Story() {
    }

    public Story(Tourist author, int authorId, String text, String name, Date submissionDate) {
        this.authorId = authorId;
        this.author = author;
        this.text = text;
        this.name = name;
        this.submissionDate = submissionDate;
    }
}
