package de.mancino.auctioneer.dto.xml;

import javax.xml.bind.annotation.XmlElement;

public class AdaptedRealmStatus {
    @XmlElement
    public long timestamp;
    @XmlElement
    public boolean isOnline;
}
