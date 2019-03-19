package ru.jakesmokie.spectre.restapi.keykeepers.planets;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Planet;
import ru.jakesmokie.spectre.entities.Race;
import ru.jakesmokie.spectre.entities.RaceAtPlanet;
import ru.jakesmokie.spectre.entities.Station;
import ru.jakesmokie.spectre.restapi.keykeepers.planets.entities.*;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/keykeepers/planets")
@Data
@Singleton
public class PlanetsResource {
    private final FailedApiResponse notAKeykeeperError =
            new FailedApiResponse("Not a keykeeper");

    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/addplanet")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse addPlanet(PlanetAddingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        val planet = new Planet(parameters.getName(), parameters.getDescription());

        em.getTransaction().begin();
        em.persist(planet);
        em.getTransaction().commit();

        return new SuccessfulApiResponse(planet);
    }

    @Path("/addstation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse addStation(StationAddingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        em.getTransaction().begin();

        val managedPlanet = em.find(Planet.class, parameters.getId());
        val stations = managedPlanet.getStations();
        stations.add(new Station("Новая станция"));

        em.getTransaction().commit();

        return new SuccessfulApiResponse("Success");
    }

    @Path("/savenamedesc")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse savePlanetNameDesc(PlanetUpdatingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        em.getTransaction().begin();

        val managedPlanet = em.find(Planet.class, parameters.getId());
        parameters.copyToPlanet(managedPlanet);

        em.getTransaction().commit();
        return new SuccessfulApiResponse("Success");
    }

    @Path("/switchplanet")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse switchPlanet(PlanetSwitchParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        em.getTransaction().begin();

        val managedPlanet = em.find(Planet.class, parameters.getId());
        managedPlanet.setDisabled(!managedPlanet.isDisabled());

        em.getTransaction().commit();
        return new SuccessfulApiResponse("Success");
    }


    @Path("/getplanets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse get(
            @QueryParam("token") String token
    ) {
        if (!auth.isKeykeeper(token)) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        val planets = em.createQuery("select p from Planet p", Planet.class)
                .setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache)
                .getResultList();

        return new SuccessfulApiResponse(planets);
    }
}
