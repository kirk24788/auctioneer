/**
 * 
 */
package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.User;
import de.mancino.auctioneer.exceptions.UserIdDoesnNotExistException;
import de.mancino.auctioneer.exceptions.UserNameAlreadyExistingException;
import de.mancino.auctioneer.exceptions.UserNameDoesnNotExistException;

/**
 *
 * @author mmancino
 */
public class UserDAOMemoryImpl implements UserDAO {
    private static final long serialVersionUID = 1L;

    protected List<User> userList;

    public UserDAOMemoryImpl(final List<User> userList) {
        this.userList = userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        synchronized (userList) {
            return Collections.unmodifiableList(new ArrayList<User>(userList));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByName(String userName) throws UserNameDoesnNotExistException {
        synchronized (userList) {
            for(User user : userList) {
                if(user.getUserName().toLowerCase().equals(userName.toLowerCase())) {
                    return user;
                }
            }
            throw new UserNameDoesnNotExistException(userName.toLowerCase());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User addUser(User user) throws UserNameAlreadyExistingException {
        synchronized (userList) {
            int nextId = 1;
            for(User u : userList) {
                if(u.getId() >= nextId) {
                    nextId = u.getId()+1;
                }
                if(u.getUserName().toLowerCase().equals(user.getUserName().toLowerCase())) {
                    throw new UserNameAlreadyExistingException(user.getUserName().toLowerCase());
                }
            }
            user.setUserName(user.getUserName().toLowerCase());
            user.setId(nextId);
            return user;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserByName(String userName) throws UserNameDoesnNotExistException {
        synchronized (userList) {
            for(User user : userList) {
                if(user.getUserName().toLowerCase().equals(userName.toLowerCase())) {
                    userList.remove(user);
                    return;
                }
            }
            throw new UserNameDoesnNotExistException(userName.toLowerCase());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserById(int userId) throws UserIdDoesnNotExistException {
        synchronized (userList) {
            for(User user : userList) {
                if(user.getId() == userId) {
                    userList.remove(user);
                    return;
                }
            }
            throw new UserIdDoesnNotExistException(userId);
        }
    }

}
