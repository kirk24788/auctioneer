package de.mancino.auctioneer.exceptions;

import de.mancino.auctioneer.dto.components.ItemName;

public class PriceWatchAlreadyExistingException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public PriceWatchAlreadyExistingException(final ItemName itemName) {
        super("PriceWatch for Item with name '" + itemName +"' does already exist!");
    }
}
