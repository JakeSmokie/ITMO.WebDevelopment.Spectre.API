package ru.jakesmokie.spectre.restapi.keykeepers.races;

import lombok.SneakyThrows;
import lombok.val;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Race;
import ru.jakesmokie.spectre.restapi.keykeepers.races.entities.RaceAddingParameters;
import ru.jakesmokie.spectre.restapi.keykeepers.races.entities.RaceUpdatingParameters;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/keykeepers/races")
@Singleton
public class RacesResource {
    private final FailedApiResponse notAKeykeeperError =
            new FailedApiResponse("Not a keykeeper");

    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/getraces")
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
        val races = em.createQuery("select r from Race r", Race.class)
                .setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache)
                .getResultList();

        return new SuccessfulApiResponse(races);
    }


    @Path("/savename")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse saveName(RaceUpdatingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        em.getTransaction().begin();

        val race = em.find(Race.class, parameters.getId());
        race.setName(parameters.getName());

        em.getTransaction().commit();
        return new SuccessfulApiResponse("Success");
    }

    @Path("/addrace")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse addPlanet(RaceAddingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return notAKeykeeperError;
        }

        val em = databaseService.getManager();
        val race = new Race("Новая раса");

        em.getTransaction().begin();
        em.persist(race);
        em.getTransaction().commit();

        return new SuccessfulApiResponse(race);
    }

}
