package ru.jakesmokie.spectre.restapi.auth;

import lombok.val;
import ru.jakesmokie.spectre.beans.AuthenticationService;
import ru.jakesmokie.spectre.restapi.auth.entities.IsAuthorizedResponse;
import ru.jakesmokie.spectre.restapi.auth.entities.LoginResponse;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Singleton
public class AuthenticationResource {
    @EJB
    private AuthenticationService authenticationService;

    @Path("/isauthorized")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public IsAuthorizedResponse isAuthorized(@QueryParam("token") String token) {
        return new IsAuthorizedResponse(authenticationService.isValidToken(token));
    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login(
            @QueryParam("login") String login,
            @QueryParam("password") String password,
            @QueryParam("realm") String realm) {
        val token = authenticationService.login(login, password, realm);
        return new LoginResponse(token.getTokenID().toString());
    }

    @Path("/logout")
    @POST
    public void logout(@QueryParam("token") String token) {
        authenticationService.logout(token);
    }
}
