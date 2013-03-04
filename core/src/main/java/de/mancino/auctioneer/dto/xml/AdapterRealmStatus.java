package de.mancino.auctioneer.dto.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.mancino.auctioneer.dto.RealmStatus;


public class AdapterRealmStatus extends XmlAdapter<AdaptedRealmStatus, RealmStatus> {

    @Override
    public RealmStatus unmarshal(final AdaptedRealmStatus realmStatus) throws Exception {
        return new RealmStatus(realmStatus.timestamp, realmStatus.isOnline);
    }

    @Override
    public AdaptedRealmStatus marshal(final RealmStatus realmStatus) throws Exception {
        AdaptedRealmStatus xmlRealmStatus = new AdaptedRealmStatus();
        xmlRealmStatus.isOnline = realmStatus.isOnline();
        xmlRealmStatus.timestamp = realmStatus.getTimestamp();
        return xmlRealmStatus;
    }

}
