package ru.jakesmokie.spectre.restapi;

import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/some")
@Singleton
public class SomeResource {
    @Path("/test")
    @GET
    public Response isAuthorized() {
        return Response.ok().build();
    }
}
