/**
 * 
 */
package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.RealmStatus;

/**
 *
 * @author mmancino
 */
public interface RealmStatusDAO extends Serializable {
    public RealmStatus getLastServerStatus();
    public List<RealmStatus> getAll();
    public void addServerStatus(final RealmStatus serverStatus);
    public void deleteAllByMaxTimestamp(final long maxTimestamp);
}
