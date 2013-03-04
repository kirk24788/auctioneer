package de.mancino.prowl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prowl {
    private static final String APPLICATION_NAME = "Auctioneer";
    private static final String PROWL_API_URL = "https://api.prowlapp.com/publicapi/add";

    private static final Logger LOG = LoggerFactory.getLogger(Prowl.class);

    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient(new ThreadSafeClientConnManager());

    private final String apiKey;

    public Prowl(final String apiKey) {
        this.apiKey = apiKey;
    }

    public void sendMessage(final Priority priority, final String event, final String description) {
        sendMessage(priority, event, description, null);
    }

    public synchronized void sendMessage(final Priority priority, final String event, final String description, final String url) {
        HttpPost postRequest = new HttpPost(PROWL_API_URL);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("apikey", apiKey));
        formparams.add(new BasicNameValuePair("priority", String.valueOf(priority.value)));
        if(url != null) {
            formparams.add(new BasicNameValuePair("url", url));
        }
        formparams.add(new BasicNameValuePair("application", APPLICATION_NAME));
        formparams.add(new BasicNameValuePair("event", event));
        formparams.add(new BasicNameValuePair("description", description));
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            final String msg = "UTF-8 not available!";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
        HttpResponse response = null;
        try {
            LOG.warn("Sending message: {} - {} (Priority: {})", new Object[]{event, description, priority.name()});
            response = HTTP_CLIENT.execute(postRequest);
            LOG.info("API Server Responded with: {}", response.getStatusLine().toString());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                if(response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
