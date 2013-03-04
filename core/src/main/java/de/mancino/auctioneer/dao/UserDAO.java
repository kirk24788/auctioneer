/**
 * 
 */
package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.User;
import de.mancino.auctioneer.exceptions.UserIdDoesnNotExistException;
import de.mancino.auctioneer.exceptions.UserNameAlreadyExistingException;
import de.mancino.auctioneer.exceptions.UserNameDoesnNotExistException;

/**
 *
 * @author mmancino
 */
public interface UserDAO extends Serializable {
    public List<User> getAll();
    public User getUserByName(final String userName) throws UserNameDoesnNotExistException;
    public User addUser(final User user) throws UserNameAlreadyExistingException;
    public void deleteUserByName(final String userName) throws UserNameDoesnNotExistException;
    public void deleteUserById(final int userId) throws UserIdDoesnNotExistException;
}
