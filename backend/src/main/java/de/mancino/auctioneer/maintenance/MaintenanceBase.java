/*
 * MainBase.java 13.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.maintenance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class MaintenanceBase {
    /**
     * Logger instance of this class.
     */
    private static final Log LOG = LogFactory.getLog(MaintenanceBase.class);

    protected final AbstractApplicationContext context;

    MaintenanceBase(String[] args) {
        final String springConfig = args.length == 0 ? "classpath:springconfig/application.xml" : args[0];
        LOG.info("Initializing Spring config '" + springConfig + "'...");
        context = new FileSystemXmlApplicationContext(springConfig);
        LOG.info("...Spring Context initialized!");
    }

    abstract void run();

    void persist() {
        LOG.info("Writing back to disk...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
    }

    void cleanup() {
        context.destroy();
        LOG.info("Finished cleanup!");
    }
}
