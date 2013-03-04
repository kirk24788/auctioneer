package de.mancino.auctioneer.dao;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;

public class RestEasyDAOSupport {
    private final String baseUrl;

    public RestEasyDAOSupport(final String baseUrl) {
        this.baseUrl =  baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    protected <T> T get(final String path, final Class<T> clazz) throws Exception {
        final ClientRequest request = new ClientRequest(baseUrl + path);
        request.accept(MediaType.APPLICATION_XML);
        final ClientResponse<T> response = request.get(clazz);
        return response.getEntity();
    }

    protected <T> List<T> getAll(final String path, final GenericType<List<T>> genericType) throws Exception {
        final ClientRequest request = new ClientRequest(baseUrl + path);
        request.accept(MediaType.APPLICATION_XML);
        final ClientResponse<List<T>> response = request.get(genericType);
        return response.getEntity();
    }

    protected <T> void post(final String path, final Object data) throws Exception {
        post(path, data, String.class);
    }

    protected <T> T post(final String path, final Object data, final Class<T> clazz) throws Exception {
        final ClientRequest request = new ClientRequest(baseUrl + path);
        request.body(MediaType.APPLICATION_XML, data);
        final ClientResponse<T> response = request.post(clazz);
        return response.getEntity();
    }

    protected void delete(final String path) throws Exception {
        delete(path, String.class);
    }

    protected <T> T delete(final String path, final Class<T> clazz) throws Exception {
        final ClientRequest request = new ClientRequest(baseUrl + path);
        request.accept(MediaType.APPLICATION_XML);
        final ClientResponse<T> response = request.delete(clazz);
        return response.getEntity();
    }
}
