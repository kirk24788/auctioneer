package de.mancino.auctioneer.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.mancino.auctioneer.bo.UserBO;
import de.mancino.auctioneer.dto.User;
import de.mancino.auctioneer.exceptions.UserNameDoesnNotExistException;
import de.mancino.auctioneer.exceptions.UserPasswordDoesntMatchException;

public class AuctioneerSession extends AuthenticatedWebSession {
    /**
     * 
     */
    private static final long serialVersionUID = 6421724381592389278L;


    /**
     * Logger instance of this class.
     */
    private static final Log LOG = LogFactory.getLog(AuctioneerSession.class);

    @SpringBean(name="userBO")
    protected UserBO userBO;

    protected User currentUser = null;

    public AuctioneerSession(final Request request) {
        super(request);
        InjectorHolder.getInjector().inject(this);
    }


    public static AuthenticatedWebSession get() {
        return (AuthenticatedWebSession) Session.get();
    }

    public static boolean isAdmin() {
        final AuctioneerSession session = (AuctioneerSession) Session.get();
        return session.currentUser != null && session.getRoles().hasRole(Roles.ADMIN);
    }

    @Override
    public boolean authenticate(final String username, final String password) {
        try {
            currentUser = userBO.authenticateUser(username, password);
            LOG.info("User '" + username + "' logged in!");
            return true;
        } catch (UserPasswordDoesntMatchException e) {
            LOG.warn("User '" + username + "' used wrong password!");
        } catch (UserNameDoesnNotExistException e) {
            LOG.warn("User '" + username + "' doesn't exist!");
        }
        currentUser = null;
        return false;
    }

    @Override
    public Roles getRoles() {
        if(isSignedIn()) {
            return new Roles(currentUser.getRoles());
        } else {
            return null;
        }
    }
    
    @Override
    public void invalidate() {
        LOG.info("User '" + currentUser.getUserName() + "' logged out.");
        super.invalidate();
        currentUser = null;
    }
}
