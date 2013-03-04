/**
 *
 */
package de.mancino.auctioneer.bo;

import de.mancino.auctioneer.dao.RealmStatusDAO;
import de.mancino.auctioneer.dto.RealmStatus;


/**
 *
 * @author mmancino
 */
public class RealmStatusBOImpl implements RealmStatusBO {

    private final RealmStatusDAO realmStatusDAO;

    public RealmStatusBOImpl(final RealmStatusDAO serverStatusDAO) {
        this.realmStatusDAO = serverStatusDAO;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastUpdate() {
        return realmStatusDAO.getLastServerStatus().getTimestamp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnline() {
        return realmStatusDAO.getLastServerStatus().isOnline();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnlineStatus(boolean isOnline, long timestamp) {
        realmStatusDAO.addServerStatus(new RealmStatus(timestamp, isOnline));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOldStatus(final long maxTimestamp) {
        realmStatusDAO.deleteAllByMaxTimestamp(maxTimestamp);
    }

}
