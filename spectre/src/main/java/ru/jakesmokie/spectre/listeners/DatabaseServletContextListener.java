package ru.jakesmokie.spectre.listeners;

import lombok.SneakyThrows;
import ru.jakesmokie.spectre.beans.DatabaseService;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseServletContextListener
        implements ServletContextListener {

    @EJB
    private DatabaseService databaseService;

    @Override
    @SneakyThrows
    public void contextInitialized(ServletContextEvent sce) {
        databaseService.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        databaseService.stop();
    }
}