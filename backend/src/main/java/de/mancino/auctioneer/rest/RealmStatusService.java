package de.mancino.auctioneer.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import de.mancino.auctioneer.dto.RealmStatus;

@Path("/realm-status")
public class RealmStatusService extends GenericRestService {

    @GET
    @Path("/last")
    @Produces("application/xml")
    public Response getStatus(@Context ServletContext servletContext) {
        return Response.status(200).entity(getRealmStatusDAO(servletContext).getLastServerStatus()).build();
    }

    @GET
    @Path("/")
    @Produces("application/xml")
    public RealmStatus[] getAll(@Context ServletContext servletContext) {
        return getRealmStatusDAO(servletContext).getAll().toArray(new RealmStatus[]{});
    }

    @DELETE
    @Path("/")
    public Response delete(@Context ServletContext servletContext, @DefaultValue("0") @QueryParam("maxAge") final long maxAge) {
        getRealmStatusDAO(servletContext).deleteAllByMaxTimestamp(maxAge);
        return Response.status(200).build();
    }

    @POST
    @Path("/")
    @Consumes("application/xml")
    public Response create(@Context ServletContext servletContext, RealmStatus realmStatus) {
        getRealmStatusDAO(servletContext).addServerStatus(realmStatus);
        return Response.status(200).build();
    }
}
