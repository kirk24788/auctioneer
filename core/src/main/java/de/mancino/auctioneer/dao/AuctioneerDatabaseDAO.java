/**
 * 
 */
package de.mancino.auctioneer.dao;

import java.io.Serializable;

/**
 *
 * @author mmancino
 */
public interface AuctioneerDatabaseDAO extends Serializable {
    public long getDatabaseSize();
}
