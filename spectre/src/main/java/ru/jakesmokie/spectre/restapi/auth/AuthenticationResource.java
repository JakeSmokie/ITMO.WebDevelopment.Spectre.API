package ru.jakesmokie.spectre.restapi.auth;

import lombok.val;
import org.eclipse.persistence.annotations.Cache;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.restapi.responses.ApiResponse;
import ru.jakesmokie.spectre.restapi.responses.SuccessfulApiResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Singleton
public class AuthenticationResource {
    @EJB
    private AuthenticationService auth;

    @Path("/isauthorized")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SuccessfulApiResponse isAuthorized(@QueryParam("token") String token) {
        return new SuccessfulApiResponse(auth.isValidToken(token));
    }

    @Path("/iskeykeeper")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SuccessfulApiResponse isKeykeeper(@QueryParam("token") String token) {
        return new SuccessfulApiResponse(auth.isKeykeeper(token));
    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse login(
            @QueryParam("login") String login,
            @QueryParam("password") String password
    ) {
        val response = auth.login(login, password);
        val tokenId = response.getAsJsonPrimitive("tokenId");

        return new ApiResponse(tokenId != null, response);
    }

    @Path("/logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public SuccessfulApiResponse logout(@QueryParam("token") String token) {
        val response = auth.logout(token);
        return new SuccessfulApiResponse(response);
    }

    @Path("/getattributes")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public SuccessfulApiResponse getAttributes(
            @QueryParam("token") String token
    ) {
        val response = auth.getSessionProperties(token);
        return new SuccessfulApiResponse(response);
    }


}
