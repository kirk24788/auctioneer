package de.mancino.auctioneer.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import de.mancino.armory.exceptions.RequestException;
import de.mancino.auctioneer.dto.AuthenticatorToken;

@Path("/authenticator")
public class AuthenticatorService extends GenericRestService {
    @GET
    @Path("/")
    @Produces("application/xml")
    public AuthenticatorToken getToken(@Context ServletContext servletContext) throws RequestException {
        return new AuthenticatorToken(getAuthenticatorVault(servletContext).getAuthenticatorToken());
    }
    @GET
    @Path("/alternative")
    @Produces("application/xml")
    public AuthenticatorToken getAlternativeToken(@Context ServletContext servletContext) throws RequestException {
        return new AuthenticatorToken(getAlternativeAuthenticatorVault(servletContext).getAuthenticatorToken());
    }
}
