package de.mancino.auctioneer.exceptions;



public class UserIdDoesnNotExistException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public UserIdDoesnNotExistException(final int userId) {
        super("User with ID '" + userId +"' doesn't exist!");
    }
}
