package de.mancino.auctioneer.rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

@Path("/errorlog")
public class ErrorLogService extends GenericRestService {

    @POST
    @Path("/")
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response insert(@Context ServletContext servletContext, ErrorEvent errorEvent) {
        return Response.status(200).entity(getErrorLogDAO(servletContext).insert(errorEvent)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Context ServletContext servletContext, @PathParam("id") final int id) {
        try {
            final ErrorEvent errorEvent = getErrorLogDAO(servletContext).getById(id);
            getErrorLogDAO(servletContext).delete(errorEvent);
            return Response.status(200).build();
        } catch (ErrorEventDoesnNotExistException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/")
    public Response deleteByTimestamp(@Context ServletContext servletContext, @DefaultValue("0") @QueryParam("maxAge") final long maxAge) {
        getErrorLogDAO(servletContext).deleteAllByMaxTimestamp(maxAge);
        return Response.status(200).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/xml")
    public Response getById(@Context ServletContext servletContext, @PathParam("id") final int id) {
        try {
            return Response.status(200).entity(getErrorLogDAO(servletContext).getById(id)).build();
        } catch (ErrorEventDoesnNotExistException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/")
    @Produces("application/xml")
    public ArrayList<ErrorEvent> create(@Context ServletContext servletContext) {
        return new ArrayList<>(getErrorLogDAO(servletContext).getAll());
    }

}
