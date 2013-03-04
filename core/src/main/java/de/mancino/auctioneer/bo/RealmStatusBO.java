/**
 *
 */
package de.mancino.auctioneer.bo;

/**
 *
 * @author mmancino
 */
public interface RealmStatusBO {
    public long getLastUpdate();
    public boolean isOnline();
    public void setOnlineStatus(final boolean isOnline, final long timestamp);
    public void deleteOldStatus(final long maxTimestamp);
}
