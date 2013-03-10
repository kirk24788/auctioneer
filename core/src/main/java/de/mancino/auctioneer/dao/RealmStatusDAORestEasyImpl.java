/**
 *
 */
package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.util.GenericType;

import de.mancino.auctioneer.dto.RealmStatus;

/**
 *
 * @author mmancino
 */
public class RealmStatusDAORestEasyImpl extends RestEasyDAOSupport implements RealmStatusDAO {
    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(RealmStatusDAORestEasyImpl.class);

    public RealmStatusDAORestEasyImpl(final String baseUrl) {
        super(baseUrl.endsWith("/") ? baseUrl + "realm-status/" : baseUrl + "/realm-status/");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealmStatus getLastServerStatus() {
        try {
            return get("last", RealmStatus.class);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new RealmStatus(0,false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealmStatus> getAll() {
        try {
            return getAll("", new GenericType<List<RealmStatus>>(){});
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addServerStatus(RealmStatus serverStatus) {
        try {
            post("", serverStatus);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        try {
            delete("?maxAge="+maxTimestamp);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
