package de.mancino.auctioneer.dto;

import de.mancino.auctioneer.dto.components.FarmStrategyId;

public class FarmStrategyLoot {
    private long id;
    private FarmStrategyId farmStrategyId;
    private int itemCount;
    private ArmoryItem item;


    public FarmStrategyLoot(final FarmStrategyId farmStrategyId, final int itemCount, final ArmoryItem item) {
        this.setFarmStrategyId(farmStrategyId);
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

    /**
     * @return the farmStrategyId
     */
    public FarmStrategyId getFarmStrategyId() {
        return farmStrategyId;
    }

    /**
     * @param farmStrategyId the farmStrategyId to set
     */
    public void setFarmStrategyId(FarmStrategyId farmStrategyId) {
        this.farmStrategyId = farmStrategyId;
    }
}
