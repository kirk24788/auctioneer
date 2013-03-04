/*
 * MyListener.java 11.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * JSTL Expression Language Registerin Listener.
 * Registers the {@link ExtendedBeanELResolver}.
 *
 * @author mmancino
 */
public  class ELResolverRegisteringListener implements ServletContextListener {
    private ServletContext context = null;

    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(ServletContextEvent event) {
        this.context = null;
    }

    /**
     * {@inheritDoc}
     */
    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();
       // JspApplicationContext jspContext = JspFactory.getDefaultFactory().getJspApplicationContext(context);
      //  jspContext.addELResolver(new ExtendedBeanELResolver());
    }
}