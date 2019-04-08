package ru.jakesmokie.spectre.restapi.tourists;

import lombok.SneakyThrows;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Race;
import ru.jakesmokie.spectre.entities.Station;
import ru.jakesmokie.spectre.entities.Tourist;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;
import ru.jakesmokie.spectre.restapi.tourists.entities.TouristRegistrationParameters;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tourists/auth")
@Singleton
public class TouristsAuthResource {
    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse register(TouristRegistrationParameters parameters) {
        if (!auth.isValidToken(parameters.getToken()) || auth.isKeykeeper(parameters.getToken())) {
            return new FailedApiResponse("Not authenticated");
        }

        val uid = auth.getUid(parameters.getToken());
        val em = databaseService.getManager();

        if (isUserRegistered(uid, em)) {
            return new FailedApiResponse("Already registered");
        }

        val station = em.find(Station.class, parameters.getStation());
        station.setPlanetId(station.getPlanet().getId());

        val tourist = new Tourist(uid,
                em.find(Race.class, parameters.getRace()),
                station);

        if (tourist.isDisabled()) {
            return new FailedApiResponse("Banned");
        }

        em.getTransaction().begin();

        tourist.setCurrentPlanet(station.getPlanet());
        tourist.setCanUseGates(false);
        tourist.setCanSendStory(true);

        em.persist(tourist);
        em.getTransaction().commit();

        return new SuccessfulApiResponse(tourist);
    }

    @Path("/isregistered")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse isRegistered(
            @QueryParam("token") String token
    ) {
        if (!auth.isValidToken(token) || auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not authenticated");
        }

        val uid = auth.getUid(token);
        val em = databaseService.getManager();

        return new SuccessfulApiResponse(isUserRegistered(uid, em));
    }

    @Path("/gettouristproperties")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse getTouristProperties(
            @QueryParam("token") String token
    ) {
        if (!auth.isValidToken(token) || auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not authenticated");
        }

        val uid = auth.getUid(token);
        val em = databaseService.getManager();

        if (!isUserRegistered(uid, em)) {
            return new FailedApiResponse("Not registered");
        }

        val tourist = em.createQuery("select t from Tourist t where t.name = :name", Tourist.class)
                .setParameter("name", uid)
                .getSingleResult();

        if (tourist.isDisabled()) {
            return new FailedApiResponse("Banned");
        }

        return new SuccessfulApiResponse(tourist);
    }

    private boolean isUserRegistered(String uid, EntityManager em) {
        return !em.createQuery("select t from Tourist t where t.name = :name", Tourist.class)
                .setParameter("name", uid)
                .getResultList()
                .isEmpty();
    }

}
