package de.mancino.auctioneer.dto;

import de.mancino.auctioneer.dto.components.SaleStrategyId;

public class SaleStrategyMaterial {
    private long id;
    private SaleStrategyId saleStrategyId;
    private int itemCount;
    private ArmoryItem item;


    public SaleStrategyMaterial(final SaleStrategyId saleStrategyId, final int itemCount, final ArmoryItem item) {
        this.saleStrategyId = saleStrategyId;
        this.itemCount = itemCount;
        this.item = item;
    }

    public ArmoryItem getItem() {
        return item;
    }
    public void setItem(ArmoryItem item) {
        this.item = item;
    }
    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public SaleStrategyId getSaleStrategyId() {
        return saleStrategyId;
    }
    public void setSaleStrategyId(SaleStrategyId saleStrategyId) {
        this.saleStrategyId = saleStrategyId;
    }
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}
