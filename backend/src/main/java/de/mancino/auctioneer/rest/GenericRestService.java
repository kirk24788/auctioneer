package de.mancino.auctioneer.rest;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.mancino.armory.AuthenticatorArmory;
import de.mancino.armory.AuthenticatorVault;
import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.RealmStatusBO;
import de.mancino.auctioneer.dao.BargainDAO;
import de.mancino.auctioneer.dao.ErrorLogDAO;
import de.mancino.auctioneer.dao.RealmStatusDAO;

public abstract class GenericRestService {
    private ApplicationContext getApplicationContext(final ServletContext servletContext) {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    protected ArmoryCharacterBO getArmoryCharacter(final ServletContext servletContext) {
        return (ArmoryCharacterBO) getApplicationContext(servletContext).getBean("armoryCharacterBO");
    }

    protected RealmStatusBO getRealmStatusBO(final ServletContext servletContext) {
        return (RealmStatusBO) getApplicationContext(servletContext).getBean("realmStatusBO");
    }

    protected RealmStatusDAO getRealmStatusDAO(final ServletContext servletContext) {
        return (RealmStatusDAO) getApplicationContext(servletContext).getBean("realmStatusDAO");
    }

    protected BargainDAO getBargainDAO(final ServletContext servletContext) {
        return (BargainDAO) getApplicationContext(servletContext).getBean("bargainDAO");
    }

    protected ErrorLogDAO getErrorLogDAO(final ServletContext servletContext) {
        return (ErrorLogDAO) getApplicationContext(servletContext).getBean("errorLogDAO");
    }

    protected AuthenticatorVault getAuthenticatorVault(final ServletContext servletContext) {
        return (AuthenticatorVault) getAuthenticatorArmory(servletContext).vault;
    }

    protected AuthenticatorArmory getAuthenticatorArmory(final ServletContext servletContext) {
        return (AuthenticatorArmory) getApplicationContext(servletContext).getBean("armory");
    }
}
