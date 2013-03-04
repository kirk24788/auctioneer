package de.mancino.auctioneer.exceptions;

import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;

public class ArmoryItemDoesNotExistException extends ASpellParserException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ArmoryItemDoesNotExistException(final ItemName itemName) {
        super("Armory Item with Name " + itemName + " does not exist");
    }

    public ArmoryItemDoesNotExistException(ArmoryId armoryId) {
        super("Armory Item with ID " + armoryId.toInt() + " does not exist");
    }

}
