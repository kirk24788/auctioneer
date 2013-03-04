package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

/**
 * PurchaseOder Id.
 *  * 
 * @author mmancino
 */
public class PurchaseOrderId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int purchaseOderId;

    /**
     * PurchaseOder Id.
     * 
     * @param armoryId Armory Id
     */
    public PurchaseOrderId(final int purchaseOderId) {
        this.purchaseOderId = purchaseOderId;
    }

    /**
     * PurchaseOder Id.
     * 
     * @param armoryId Armory Id
     */
    public static PurchaseOrderId purchaseOrderId(final int purchaseOderId) {
        return new PurchaseOrderId(purchaseOderId);
    }

    /**
     * Gibt den int-Wert der PurchaseOder ID zurück.
     * 
     * @return Armory ID
     */
    public int toInt() {
        return purchaseOderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return purchaseOderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PurchaseOrderId && ((PurchaseOrderId)obj).purchaseOderId == purchaseOderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(purchaseOderId);
    }
}