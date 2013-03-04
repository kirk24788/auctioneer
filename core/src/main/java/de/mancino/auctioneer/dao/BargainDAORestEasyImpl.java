package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.util.GenericType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.exceptions.BargainDoesnNotExistException;

public class BargainDAORestEasyImpl extends RestEasyDAOSupport implements BargainDAO {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BargainDAORestEasyImpl.class);

    public BargainDAORestEasyImpl(final String baseUrl) {
        super(baseUrl.endsWith("/") ? baseUrl + "bargains/" : baseUrl + "/bargains/");
    }

    @Override
    public Bargain insert(final Bargain deal) {
        try {
            return post("bargain", deal, Bargain.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void delete(Bargain deal) {
        try {
            delete("bargain/"+deal.getId());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        try {
            delete("bargains/"+maxTimestamp);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Bargain getById(int id) throws BargainDoesnNotExistException {
        try {
            return get("bargain/"+id, Bargain.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new BargainDoesnNotExistException(id);
        }

    }

    @Override
    public List<Bargain> getAll() {
        try {
            return getAll("bargains", new GenericType<List<Bargain>>(){});
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public int getSize() {
        try {
            return getAll("bargains", new GenericType<List<Bargain>>(){}).size();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0;
        }
    }
}
