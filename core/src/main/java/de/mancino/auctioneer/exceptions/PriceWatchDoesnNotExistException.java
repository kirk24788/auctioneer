package de.mancino.auctioneer.exceptions;

import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;

public class PriceWatchDoesnNotExistException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public PriceWatchDoesnNotExistException(final ItemName itemName) {
        super("Price Watch for Item '" + itemName +"' doesn't exist!");
    }

    public PriceWatchDoesnNotExistException(final ArmoryId armoryId) {
        super("Price Watch for Item with ID " + armoryId.toInt() +" doesn't exist!");
    }
}
