package ru.jakesmokie.spectre.beans;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.DriverManager;

@Singleton
public class DatabaseService {
    private EntityManager manager;
    private EntityManagerFactory factory;

    public EntityManager getManager() {
        return manager;
    }

    public void start() {
        factory = Persistence.createEntityManagerFactory("TheSpectreJPA");
        manager = factory.createEntityManager();
    }

    @SneakyThrows
    public void stop() {
        if (factory.isOpen()) {
            factory.close();
        }

        if (manager.isOpen()) {
            manager.close();
        }
    }
}
