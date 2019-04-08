package ru.jakesmokie.spectre.restapi.keykeepers.stories;

import lombok.SneakyThrows;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Story;
import ru.jakesmokie.spectre.restapi.keykeepers.tourists.entities.StoryRatingParameters;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.FailedApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/keykeepers/stories")
public class KeykeepersStoriesResource {
    @EJB
    private AuthenticationService auth;

    @EJB
    private DatabaseService databaseService;

    @Path("/getstories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse get(
            @QueryParam("token") String token
    ) {
        if (!auth.isKeykeeper(token)) {
            return new FailedApiResponse("Not a keykeeper");
        }

        val em = databaseService.getManager();
        val stories = em.createQuery("select s from Story s", Story.class)
                .getResultList();

        return new SuccessfulApiResponse(stories);
    }

    @Path("/ratestory")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse rateStory(StoryRatingParameters parameters) {
        if (!auth.isKeykeeper(parameters.getToken())) {
            return new FailedApiResponse("Not a keykeeper");
        }

        if (parameters.getScore() < 0) {
            return new FailedApiResponse("Wrong parameters");
        }

        val em = databaseService.getManager();
        val story = em.find(Story.class, parameters.getId());
        val author = story.getAuthor();

        if (story.isAccepted()) {
            return new FailedApiResponse("Story already rated");
        }

        em.getTransaction().begin();

        story.setAccepted(true);
        story.setScore(parameters.getScore());

        val canUseGates = parameters.getScore() != 0;

        author.setScore(author.getScore() + parameters.getScore());
        author.setCanUseGates(canUseGates);
        author.setCanSendStory(!canUseGates);

        em.getTransaction().commit();

        return new SuccessfulApiResponse("Success");
    }
}
