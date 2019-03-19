package ru.jakesmokie.spectre.restapi.keykeepers.races;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.beans.DatabaseService;
import ru.jakesmokie.spectre.entities.Planet;
import ru.jakesmokie.spectre.entities.Race;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/keykeepers/races")
@Data
@Singleton
public class RacesResource {
    @EJB
    private AuthenticationService authenticationService;

    @EJB
    private DatabaseService databaseService;


    @Path("/getraces")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public ApiResponse get(
            @QueryParam("token") String token
    ) {
//        if (!auth.isKeykeeper(token)) {
//            return notAKeykeeperError;
//        }

        val em = databaseService.getManager();
        val races = em.createQuery("select r from Race r", Race.class)
                .setHint(QueryHints.CACHE_USAGE, CacheUsage.DoNotCheckCache)
                .getResultList();

        return new SuccessfulApiResponse(races);
    }
}
