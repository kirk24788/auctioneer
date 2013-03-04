package de.mancino.auctioneer.exceptions;


import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.RealmName;

public class ArmoryCharacterAlreadyExistingException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public ArmoryCharacterAlreadyExistingException(final CharacterName characterName, final RealmName realmName) {
        super("PriceWatch for Item with Name '" + characterName + "' on Realm '" + realmName + " does already exist!");
    }
}
