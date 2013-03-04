package de.mancino.auctioneer.exceptions;



public class UserNameDoesnNotExistException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public UserNameDoesnNotExistException(final String userName) {
        super("User with Name '" + userName + "' doesn't exist!");
    }
}
