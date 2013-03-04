/**
 *
 */
package de.mancino.auctioneer.bo;

import de.mancino.auctioneer.dao.UserDAO;
import de.mancino.auctioneer.dto.User;
import de.mancino.auctioneer.exceptions.UserNameDoesnNotExistException;
import de.mancino.auctioneer.exceptions.UserPasswordDoesntMatchException;


/**
 *
 * @author mmancino
 */
public class UserBOImpl implements UserBO {
    private static final long serialVersionUID = 1L;

    private final UserDAO userDAO;

    public UserBOImpl(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User authenticateUser(String username, String password) throws UserPasswordDoesntMatchException, UserNameDoesnNotExistException {
        for(final User user : userDAO.getAll()) {
            if(user.getUserName().equalsIgnoreCase(username)) {
                if(user.getPassword().equals(password)) {
                    return user;
                } else {
                    throw new UserPasswordDoesntMatchException(username);
                }
            }
        }
        throw new UserNameDoesnNotExistException(username);
    }
}
