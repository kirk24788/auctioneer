package de.mancino.auctioneer.exceptions;


import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.RealmName;

public class ArmoryCharacterDoesnNotExistException extends Exception {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    public ArmoryCharacterDoesnNotExistException(final CharacterName characterName, final RealmName realmName) {
        super("Armory Character with Name '" + characterName +"' on Realm '" + realmName + "' doesn't exist!");
    }
}
