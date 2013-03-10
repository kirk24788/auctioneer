package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.util.GenericType;

import de.mancino.auctioneer.dto.ErrorEvent;
import de.mancino.auctioneer.exceptions.ErrorEventDoesnNotExistException;

public class ErrorLogDAORestEasyImpl extends RestEasyDAOSupport implements ErrorLogDAO {
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(ErrorLogDAORestEasyImpl.class);

    public ErrorLogDAORestEasyImpl(final String baseUrl) {
        super(baseUrl.endsWith("/") ? baseUrl + "errorlog/" : baseUrl + "/errorlog/");
    }

    @Override
    public ErrorEvent insert(final ErrorEvent errorEvent) {
        try {
            return post("", errorEvent, ErrorEvent.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void delete(ErrorEvent deal) {
        try {
            delete(String.valueOf(deal.getId()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        try {
            delete("?maxAge="+maxTimestamp);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public ErrorEvent getById(int id) throws ErrorEventDoesnNotExistException {
        try {
            return get(String.valueOf(id), ErrorEvent.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ErrorEventDoesnNotExistException(id);
        }

    }

    @Override
    public List<ErrorEvent> getAll() {
        try {
            return getAll("", new GenericType<List<ErrorEvent>>(){});
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public int getSize() {
        return getAll().size();
    }
}
