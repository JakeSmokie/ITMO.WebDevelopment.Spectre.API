package ru.jakesmokie.spectre.restapi.keykeepers.races;

import lombok.Data;
import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.restapi.keykeepers.planets.PlanetAddingParameters;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/keykeepers/races")
@Data
public class RacesResource {
    @EJB
    private AuthenticationService authenticationService;

    @EJB
    private DatabaseService databaseService;

    @Path("/addplanet")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlanet(
            @QueryParam("params") String plainParams
    ) {
        val parameters = PlanetAddingParameters.fromJson(plainParams);

        return Response.ok()
                .build();
    }


}
