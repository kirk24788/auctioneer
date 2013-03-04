package de.mancino.auctioneer.exceptions;

import de.mancino.armory.xml.wowhead.item.Item;


public class WowHeadItemAlreadyExistingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WowHeadItemAlreadyExistingException(final Item item) {
        super("Wow-Head Item with ID " + item.id + " does already exist!");
    }

}