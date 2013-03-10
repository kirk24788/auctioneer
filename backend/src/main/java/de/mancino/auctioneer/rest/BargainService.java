package de.mancino.auctioneer.rest;

import java.util.ArrayList;
import java.util.List;

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

import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.exceptions.BargainDoesnNotExistException;

@Path("/bargains")
public class BargainService extends GenericRestService {
    @POST
    @Path("/")
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response insert(@Context ServletContext servletContext, Bargain deal) {
        return Response.status(200).entity(getBargainDAO(servletContext).insert(deal)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Context ServletContext servletContext, @PathParam("id") final int id) {
        try {
            final Bargain deal = getBargainDAO(servletContext).getById(id);
            getBargainDAO(servletContext).delete(deal);
            return Response.status(200).build();
        } catch (BargainDoesnNotExistException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/")
    public Response deleteByTimestamp(@Context ServletContext servletContext, @DefaultValue("0") @QueryParam("maxAge") final long maxAge) {
        getBargainDAO(servletContext).deleteAllByMaxTimestamp(maxAge);
        return Response.status(200).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/xml")
    public Response getById(@Context ServletContext servletContext, @PathParam("id") final int id) {
        try {
            return Response.status(200).entity(getBargainDAO(servletContext).getById(id)).build();
        } catch (BargainDoesnNotExistException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/")
    @Produces("application/xml")
    public List<Bargain> create(@Context ServletContext servletContext) {
        return new ArrayList<>(getBargainDAO(servletContext).getAll());
    }
}
