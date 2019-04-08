package ru.jakesmokie.spectre.restapi.tourists;

import lombok.SneakyThrows;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Station;
import ru.jakesmokie.spectre.entities.Tourist;
import ru.jakesmokie.spectre.entities.Travel;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;
import ru.jakesmokie.spectre.restapi.tourists.entities.GatesUsingParameters;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/tourists/travels")
@Singleton
public class TouristsTravelsResource {
    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/gettravels")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse get(
            @QueryParam("token") String token
    ) {
        if (!auth.isValidToken(token) || auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not authenticated");
        }

        val uid = auth.getUid(token);
        val em = databaseService.getManager();
        val tourist = getTourist(uid, em);

        if (tourist == null) {
            return new FailedApiResponse("Not registered");
        }

        if (tourist.isDisabled()) {
            return new FailedApiResponse("Banned");
        }

        val travels = em.createQuery("select t from Travel t where t.tourist = :tourist", Tourist.class)
                .setParameter("tourist", tourist)
                .getResultList();

        return new SuccessfulApiResponse(travels);
    }

    @Path("/usegates")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse useGates(GatesUsingParameters parameters) {
        if (!auth.isValidToken(parameters.getToken()) || auth.isKeykeeper(parameters.getToken())) {
            return new FailedApiResponse("Not authenticated");
        }

        if (parameters.getOrigin() == parameters.getDestination()) {
            return new FailedApiResponse("Origin cannot be equal to destination.");
        }

        val uid = auth.getUid(parameters.getToken());
        val em = databaseService.getManager();
        val tourist = getTourist(uid, em);

        if (tourist == null) {
            return new FailedApiResponse("Not registered");
        }

        if (tourist.isDisabled()) {
            return new FailedApiResponse("Banned");
        }

        if (!tourist.isCanUseGates()) {
            return new FailedApiResponse("Cannot use gates");
        }

        val origin = em.find(Station.class, parameters.getOrigin());
        val destination = em.find(Station.class, parameters.getDestination());

        if (origin.getPlanet().isDisabled() || destination.getPlanet().isDisabled()) {
            return new FailedApiResponse("Planet disabled");
        }

        if (origin.getPlanet().getId() == destination.getPlanet().getId()) {
            return new FailedApiResponse("Origin planet cannot be equal to destination.");
        }

        if (origin.getPlanet().getId() != tourist.getCurrentPlanet().getId()) {
            return new FailedApiResponse("Security violation");
        }

        val travel = new Travel(tourist, origin, destination, new Date());
        em.getTransaction().begin();

        em.persist(travel);
        tourist.setCanSendStory(true);
        tourist.setCanUseGates(tourist.getScore() >= 100);
        tourist.setCurrentPlanet(destination.getPlanet());

        em.getTransaction().commit();
        return new SuccessfulApiResponse(travel);
    }

    private Tourist getTourist(String uid, EntityManager em) {
        return em.createQuery("select t from Tourist t where t.name = :name", Tourist.class)
                .setParameter("name", uid)
                .getSingleResult();
    }
}
