package de.mancino.auctioneer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.auctioneer.strategies.BuyoutStrategy;

public class PurchaseOrder implements Serializable {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    private PurchaseOrderId id;

    private ArmoryItem armoryItem;

    private int itemCount;

    private Currency maxPrice;

    private boolean active;

    private long timeInMilliseconds;

    private BuyoutStrategy strategy;

    private List<Purchase> purchases;


    public PurchaseOrder() {
        this(null, 0, new Currency(0));
    }

    public PurchaseOrder(final ArmoryItem armoryItem, final int itemCount, final Currency maxPrice) {
        this.armoryItem = armoryItem;
        this.itemCount = itemCount;
        this.maxPrice = maxPrice;
        this.active = true;
        this.timeInMilliseconds = System.currentTimeMillis();
        this.purchases = new ArrayList<Purchase>();
    }

    /**
     * @return the id
     */
    public PurchaseOrderId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(PurchaseOrderId id) {
        this.id = id;
    }

    /**
     * @return the armoryItem
     */
    public ArmoryItem getArmoryItem() {
        return armoryItem;
    }

    /**
     * @param armoryItem the armoryItem to set
     */
    public void setArmoryItem(ArmoryItem armoryItem) {
        this.armoryItem = armoryItem;
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
     * @return the maxPrice
     */
    public Currency getMaxPrice() {
        return maxPrice;
    }

    /**
     * @param maxPrice the maxPrice to set
     */
    public void setMaxPrice(Currency maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
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
     * @return the strategy
     */
    public BuyoutStrategy getStrategy() {
        return strategy;
    }

    /**
     * @param strategy the strategy to set
     */
    public void setStrategy(BuyoutStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * @return the purchases
     */
    public List<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * @param purchases the purchases to set
     */
    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public int getItemsBought() {
        int sum = 0;
        for(Purchase p : purchases) {
            sum += p.getItemCount();
        }
        return sum;
    }
}
