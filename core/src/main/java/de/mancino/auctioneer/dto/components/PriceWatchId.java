package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

/**
 * PriceWatch Id.
 *  * 
 * @author mmancino
 */
public class PriceWatchId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int priceWatchId;

    /**
     * PriceWatch Id.
     * 
     * @param armoryId Armory Id
     */
    public PriceWatchId(final int priceWatchId) {
        this.priceWatchId = priceWatchId;
    }

    /**
     * PriceWatch Id.
     * 
     * @param armoryId Armory Id
     */
    public static PriceWatchId priceWatchId(final int priceWatchId) {
        return new PriceWatchId(priceWatchId);
    }

    /**
     * Gibt den int-Wert der PriceWatch ID zurück.
     * 
     * @return Armory ID
     */
    public int toInt() {
        return priceWatchId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return priceWatchId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PriceWatchId && ((PriceWatchId)obj).priceWatchId == priceWatchId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(priceWatchId);
    }
}