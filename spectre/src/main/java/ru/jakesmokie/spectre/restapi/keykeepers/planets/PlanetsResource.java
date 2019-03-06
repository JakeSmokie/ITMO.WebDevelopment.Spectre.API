package ru.jakesmokie.spectre.restapi.keykeepers.planets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Planet;
import ru.jakesmokie.spectre.exceptions.InvalidTokenException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/keykeepers/planets")
@Data
public class PlanetsResource {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    @EJB
    private AuthenticationService authenticationService;

    @EJB
    private DatabaseService databaseService;

    @Path("/addplanet")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlanet(
            PlanetAddingParameters parameters
    ) {
        if (!authenticationService.isValidToken(parameters.getToken())) {
            return Response.status(400).build();
        }

        val planet = parameters.toPlanet();

        val em = databaseService.getManager();
        em.getTransaction().begin();
        em.persist(planet);
        em.getTransaction().commit();

        return Response.ok()
                .build();
    }

    @Path("/getplanets")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public List<Planet> getPlanets(
            @QueryParam("token") String token
    ) {
        if (!authenticationService.isValidToken(token)) {
            throw new InvalidTokenException();
        }

        val em = databaseService.getManager();
        return em.createQuery("select p from Planet p", Planet.class)
                .getResultList();
    }
}
