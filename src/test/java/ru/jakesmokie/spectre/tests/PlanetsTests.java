package ru.jakesmokie.spectre.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.val;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.restapi.keykeepers.planets.PlanetAddingParameters;
import ru.jakesmokie.spectre.restapi.keykeepers.planets.PlanetsResource;

import java.util.Date;

public class PlanetsTests {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final AuthenticationService authenticationService = new AuthenticationService();
    private static PlanetsResource planetsResource;

    @BeforeClass
    public static void init() {
        planetsResource = new PlanetsResource();
        planetsResource.setAuthenticationService(authenticationService);
        planetsResource.setDatabaseService(new DatabaseService());
    }

    @Test
    public void testAddingGettingAndDeleting() {
        val token = authenticationService.login("1", "12345678", "/keykeepers")
                .getTokenID().toString();
        val parameters = new PlanetAddingParameters(token, "Earth", "...", new Date());

        planetsResource.addPlanet(parameters);
        val planets = planetsResource.getPlanets(token);

        Assert.assertTrue(planets.stream()
                .anyMatch(x -> x.getName().equals("Earth"))
        );
    }
}
