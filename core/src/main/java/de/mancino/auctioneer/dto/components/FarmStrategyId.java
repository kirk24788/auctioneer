package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

/**
 * FarmStrategy Id.
 *  *
 * @author mmancino
 */
public class FarmStrategyId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int farmStrategyId;

    /**
     * FarmStrategy Id.
     *
     * @param farmStrategyId FarmStrategy Id
     */
    public FarmStrategyId(final int farmStrategyId) {
        this.farmStrategyId = farmStrategyId;
    }

    /**
     * FarmStrategy Id.
     *
     * @param farmStrategyId FarmStrategy Id
     */
    public static FarmStrategyId farmStrategyId(final int farmStrategyId) {
        return new FarmStrategyId(farmStrategyId);
    }

    /**
     * Gibt den int-Wert der FarmStrategy ID zurück.
     *
     * @return Armory ID
     */
    public int toInt() {
        return farmStrategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return farmStrategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FarmStrategyId && ((FarmStrategyId)obj).farmStrategyId == farmStrategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(farmStrategyId);
    }
}