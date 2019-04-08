package ru.jakesmokie.spectre.restapi.keykeepers.planets;

import lombok.SneakyThrows;
import lombok.val;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.*;
import ru.jakesmokie.spectre.restapi.keykeepers.planets.entities.*;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/keykeepers/planets")
@Singleton
public class PlanetsResource {
    private final FailedApiResponse notAKeykeeperError =
            new FailedApiResponse("Not a keykeeper");

    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/getplanets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse get(
            @QueryParam("token") String token
    ) {
        if (!auth.isValidToken(token)) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        val planets = em.createQuery("select p from Planet p", Planet.class)
                .setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache)
                .getResultList();

        return new SuccessfulApiResponse(planets);
    }

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

        val planet = em.find(Planet.class, parameters.getId());
        val stations = planet.getStations();

        val station = new Station("Новая станция");
        station.setPlanet(planet);
        stations.add(station);
        em.persist(station);

        em.getTransaction().commit();

        return new SuccessfulApiResponse(station);
    }

    @Path("/updatestationname")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse updateStationName(StationUpdatingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        em.getTransaction().begin();

        val station = em.find(Station.class, parameters.getStation());
        station.setName(parameters.getName());

        em.getTransaction().commit();
        return new SuccessfulApiResponse(station);
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

    @Path("/updateracedangerlevel")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse updateRaceDangerLevel(RaceLevelUpdatingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }


        val em = databaseService.getManager();
        em.getTransaction().begin();

        val key = new RaceAtPlanetKey(parameters.getRace(), parameters.getPlanet());
        val raceAtPlanet = em.find(RaceAtPlanet.class, key);

        if (raceAtPlanet != null) {
            raceAtPlanet.setDangerLevel(parameters.getLevel());
        } else {
            val planet = em.find(Planet.class, parameters.getPlanet());
            val newRaceAtPlanet = new RaceAtPlanet(
                    key,
                    parameters.getLevel(),
                    planet,
                    em.find(Race.class, parameters.getRace())
            );

            em.persist(newRaceAtPlanet);
            planet.getRaces().add(newRaceAtPlanet);
        }

        em.getTransaction().commit();
        return new SuccessfulApiResponse("Success");
    }

}
