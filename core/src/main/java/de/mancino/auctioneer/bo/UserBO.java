/**
 *
 */
package de.mancino.auctioneer.bo;

import java.io.Serializable;

import de.mancino.auctioneer.dto.User;
import de.mancino.auctioneer.exceptions.UserNameDoesnNotExistException;
import de.mancino.auctioneer.exceptions.UserPasswordDoesntMatchException;

/**
 *
 * @author mmancino
 */
public interface UserBO extends Serializable {
    public User authenticateUser(final String username, final String password)
            throws UserPasswordDoesntMatchException, UserNameDoesnNotExistException;
}
