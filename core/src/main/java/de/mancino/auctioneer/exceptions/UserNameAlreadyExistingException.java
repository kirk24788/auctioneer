package de.mancino.auctioneer.exceptions;



public class UserNameAlreadyExistingException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public UserNameAlreadyExistingException(final String userName) {
        super("User with Name '" + userName + "' does already exist!");
    }
}
