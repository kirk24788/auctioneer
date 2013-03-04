/**
 * 
 */
package de.mancino.auctioneer.exceptions;

/**
 *
 * @author mmancino
 */
public class UserPasswordDoesntMatchException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public UserPasswordDoesntMatchException(final String userName) {
        super("User '" + userName + "' tried logging in with wrong password!");
    }
}