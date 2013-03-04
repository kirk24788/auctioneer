/**
 *
 */
package de.mancino.auctioneer.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mmancino
 */
@XmlRootElement(name = "realmstatus")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RealmStatus implements Comparable<RealmStatus>, Serializable  {
    private static final long serialVersionUID = 1L;
    private long timestamp;
    private boolean isOnline;

    public RealmStatus() {
    }

    public RealmStatus(final long timestamp, final boolean isOnline) {
        this.setTimestamp(timestamp);
        this.setOnline(isOnline);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final RealmStatus other) {
        if ( getTimestamp() == other.getTimestamp()) {
            return 0;
        } else {
            return getTimestamp() < other.getTimestamp() ? -1 : 1;
        }
    }

    /**
     * @return the isOnline
     */
    @XmlElement
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * @param isOnline the isOnline to set
     */
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * @return the timestamp
     */
    @XmlElement
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
