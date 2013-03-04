package de.mancino.auctioneer.exceptions;


public class BargainDoesnNotExistException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public BargainDoesnNotExistException(final int id) {
        super("Deal with ID " + id +" doesn't exist!");
    }
}
