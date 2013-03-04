package de.mancino.auctioneer.dto.xml;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.mancino.auctioneer.dto.RealmStatus;
@XmlRootElement(name = "realmstatus")
public class XmlRealmStatus {
    @XmlJavaTypeAdapter(AdapterRealmStatus.class)
    public RealmStatus realmStatus;

    public XmlRealmStatus(final RealmStatus realmStatus) {
        this.realmStatus = realmStatus;
    }

    public XmlRealmStatus() {
    }
}
