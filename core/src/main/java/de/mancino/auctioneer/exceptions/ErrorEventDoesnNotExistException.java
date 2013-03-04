package de.mancino.auctioneer.exceptions;


public class ErrorEventDoesnNotExistException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public ErrorEventDoesnNotExistException(final int id) {
        super("Error Event with ID " + id +" doesn't exist!");
    }
}
