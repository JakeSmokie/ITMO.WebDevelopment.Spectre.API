package ru.jakesmokie.spectre.restapi.keykeepers.tourists;

import lombok.SneakyThrows;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Tourist;
import ru.jakesmokie.spectre.entities.Travel;
import ru.jakesmokie.spectre.restapi.keykeepers.tourists.entities.TouristSwitchingParameters;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/keykeepers/tourists")
public class KeykeepersTouristsResource {
    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/gettourists")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse getTourists(
            @QueryParam("token") String token
    ) {
        if (!auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not a keykeeper");
        }

        val em = databaseService.getManager();
        val tourists = em.createQuery("select s from Tourist s", Tourist.class)
                .getResultList();

        tourists.forEach(t -> {
            val props = auth.getUserProperties(t.getName());
            t.setProps(props);
        });

        return new SuccessfulApiResponse(tourists);
    }

    @Path("/gettravels")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse getTravels(
            @QueryParam("token") String token
    ) {
        if (!auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not a keykeeper");
        }

        val em = databaseService.getManager();
        val travels = em.createQuery("select s from Travel s", Travel.class)
                .getResultList();

        return new SuccessfulApiResponse(travels);
    }

    @Path("/switchtourist")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse switchTourist(TouristSwitchingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return new FailedApiResponse("Not a keykeeper");
        }

        val em = databaseService.getManager();
        em.getTransaction().begin();

        val tourist = em.find(Tourist.class, parameters.getId());
        tourist.setDisabled(!tourist.isDisabled());

        em.getTransaction().commit();
        return new SuccessfulApiResponse(tourist);
    }
}
