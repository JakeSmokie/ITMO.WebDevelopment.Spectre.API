package ru.jakesmokie.spectre.restapi.tourists;


import lombok.SneakyThrows;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Story;
import ru.jakesmokie.spectre.entities.Tourist;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;
import ru.jakesmokie.spectre.restapi.tourists.entities.TouristsStorySendingParameters;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/tourists/stories")
@Singleton
public class TouristsStoriesResource {
    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/cansendstory")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse canSendStory(
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

        return new SuccessfulApiResponse(tourist.isCanSendStory());
    }

    @Path("/sendstory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse sendStory(TouristsStorySendingParameters parameters) {
        val token = parameters.getToken();

        if (!auth.isValidToken(token) || auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not authenticated");
        }

        val uid = auth.getUid(token);
        val em = databaseService.getManager();

        if (!isUserRegistered(uid, em)) {
            return new FailedApiResponse("Not registered");
        }

        val tourist = getTourist(uid, em);

        if (tourist == null) {
            return new FailedApiResponse("Not registered");
        }

        if (tourist.isDisabled()) {
            return new FailedApiResponse("Banned");
        }

        if (!tourist.isCanSendStory()) {
            return new FailedApiResponse("Story is already sent");
        }

        val story = new Story(tourist, tourist.getId(), parameters.getText(),
                parameters.getName(), new Date());

        em.getTransaction().begin();
        em.persist(story);
        tourist.setCanSendStory(tourist.getScore() >= 100);
        em.getTransaction().commit();

        return new SuccessfulApiResponse(story);
    }

    @Path("/getstories")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
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

        if (!isUserRegistered(uid, em)) {
            return new FailedApiResponse("Not registered");
        }

        val tourist = getTourist(uid, em);

        if (tourist == null) {
            return new FailedApiResponse("Not registered");
        }

        if (tourist.isDisabled()) {
            return new FailedApiResponse("Banned");
        }

        val stories = em.createQuery("select s from Story s where " +
                "s.author = :author", Story.class)
                .setParameter("author", tourist)
                .getResultList();

        return new SuccessfulApiResponse(stories);
    }


    private Tourist getTourist(String uid, EntityManager em) {
        return em.createQuery("select t from Tourist t where t.name = :name", Tourist.class)
                .setParameter("name", uid)
                .getSingleResult();
    }

    private boolean isUserRegistered(String uid, EntityManager em) {
        return !em.createQuery("select t from Tourist t where t.name = :name", Tourist.class)
                .setParameter("name", uid)
                .getResultList()
                .isEmpty();
    }
}
