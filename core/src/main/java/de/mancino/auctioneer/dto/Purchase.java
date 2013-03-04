package de.mancino.auctioneer.dto;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;



public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L;

    private PurchaseOrderId purchaseOrderId;
    private int itemCount;
    private Currency price;
    private long timeInMilliseconds;
    private String buyerName;

    /**
     * @return the purchaseOrderId
     */
    public PurchaseOrderId getPurchaseOrderId() {
        return purchaseOrderId;
    }
    /**
     * @param purchaseOrderId the purchaseOrderId to set
     */
    public void setPurchaseOrderId(PurchaseOrderId purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    /**
     * @return the itemCount
     */
    public int getItemCount() {
        return itemCount;
    }
    /**
     * @param itemCount the itemCount to set
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    /**
     * @return the price
     */
    public Currency getPrice() {
        return price;
    }
    /**
     * @param price the price to set
     */
    public void setPrice(Currency price) {
        this.price = price;
    }
    /**
     * @return the timeInMilliseconds
     */
    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }
    /**
     * @param timeInMilliseconds the timeInMilliseconds to set
     */
    public void setTimeInMilliseconds(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }
    /**
     * @return the buyerName
     */
    public String getBuyerName() {
        return buyerName;
    }
    /**
     * @param buyerName the buyerName to set
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

}
