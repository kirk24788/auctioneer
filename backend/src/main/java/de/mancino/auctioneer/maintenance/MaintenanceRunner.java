/*
 * MaintenanceMain.java 13.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.maintenance;

public class MaintenanceRunner {

    /**
     * @param args
     */
    public static void run(MaintenanceBase runnable) {
        try {
            runnable.run();
        } finally {
            runnable.cleanup();
        }
    }

}
