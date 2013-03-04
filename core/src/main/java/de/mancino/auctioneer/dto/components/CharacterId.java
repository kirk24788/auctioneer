package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

/**
 * PriceWatch Id.
 *  * 
 * @author mmancino
 */
public class CharacterId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int characterId;

    /**
     * PriceWatch Id.
     * 
     * @param armoryId Armory Id
     */
    public CharacterId(final int characterId) {
        this.characterId = characterId;
    }

    /**
     * PriceWatch Id.
     * 
     * @param armoryId Armory Id
     */
    public static CharacterId characterId(final int characterId) {
        return new CharacterId(characterId);
    }

    /**
     * Gibt den int-Wert der PriceWatch ID zurück.
     * 
     * @return Armory ID
     */
    public int toInt() {
        return characterId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return characterId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CharacterId && ((CharacterId)obj).characterId == characterId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(characterId);
    }
}