package de.mancino.auctioneer.exceptions;

import de.mancino.armory.json.api.item.Item;


public class ArmoryItemAlreadyExistingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ArmoryItemAlreadyExistingException(final Item item) {
        super("Armory Item with ID " + item.id + " does already exist!");
    }

}
