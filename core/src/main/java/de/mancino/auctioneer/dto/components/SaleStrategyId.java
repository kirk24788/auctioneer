package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

/**
 * SaleStrategy Id.
 *  * 
 * @author mmancino
 */
public class SaleStrategyId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int saleStrategyId;

    /**
     * SaleStrategy Id.
     * 
     * @param saleStrategyId SaleStrategy Id
     */
    public SaleStrategyId(final int saleStrategyId) {
        this.saleStrategyId = saleStrategyId;
    }

    /**
     * SaleStrategy Id.
     * 
     * @param saleStrategyId SaleStrategy Id
     */
    public static SaleStrategyId saleStrategyId(final int saleStrategyId) {
        return new SaleStrategyId(saleStrategyId);
    }

    /**
     * Gibt den int-Wert der SaleStrategy ID zurück.
     * 
     * @return Armory ID
     */
    public int toInt() {
        return saleStrategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return saleStrategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof SaleStrategyId && ((SaleStrategyId)obj).saleStrategyId == saleStrategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(saleStrategyId);
    }
}