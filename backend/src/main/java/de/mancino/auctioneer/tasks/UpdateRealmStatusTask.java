/*
 * UpdatePriceWatch.java 11.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.tasks;

import de.mancino.armory.Armory;
import de.mancino.armory.json.api.realm.RealmStatus;
import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.bo.RealmStatusBO;
import de.mancino.prowl.Priority;
import de.mancino.prowl.Prowl;

/**
 * Task for updating the cash log.
 *
 * @author mmancino
 */
public class UpdateRealmStatusTask extends AuctioneerTask {
    /**
     * Armory
     */
    private final Armory armory;

    private final RealmStatusBO realmStatusBO;

    private final String realm;

    private final Prowl prowl;

    private boolean lastStatus = true;

    public UpdateRealmStatusTask(final RealmStatusBO realmStatusBO, final Armory armory, final String realm, final Prowl prowl, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.realmStatusBO = realmStatusBO;
        this.armory = armory;
        this.realm = realm;
        this.prowl = prowl;
        prowl.sendMessage(Priority.NORMAL, "Auctioneer Started!", "Auctioneer BackEnd Service has been started.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInternal() {
        boolean newStatus = isOnline(3);
        realmStatusBO.setOnlineStatus(newStatus, System.currentTimeMillis());
        if(newStatus != lastStatus) {
            final Priority priority = newStatus ? Priority.HIGH : Priority.MODERATE;
            final String event = "Realm '" + realm + "' is " + (newStatus ? "ONLINE" : "OFFLINE");
            final String description = "Realm-Status of realm '" + realm + "' has changed to " + (newStatus ? "online" : "offline");
            prowl.sendMessage(priority, event, description);
            lastStatus = newStatus;
        }
    }

    private boolean isOnline(final int retriesLeft) {
        if(retriesLeft<=0) {
            return false;
        }
        try {
            final RealmStatus status = armory.api.getRealmStatus(realm);
            return status.realms.get(0).status;
        } catch (Throwable t) {
            return isOnline(retriesLeft-1);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "RealmStatus Update";
    }
}
