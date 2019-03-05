package ru.jakesmokie.spectre.beans;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class DatabaseService {
    private final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("JPAUNIT");
    private final EntityManager manager = factory.createEntityManager();

    public EntityManager getManager() {
        return manager;
    }
}
