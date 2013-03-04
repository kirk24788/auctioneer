package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.FarmStrategyId;

public interface FarmStrategyBO extends Serializable {
    public FarmStrategy createFarmStrategy(final ArmoryItem iconItemId, final String name, Currency additionalProfits,
            List<FarmStrategyLoot> loot);
    public void addLoot(final FarmStrategyId farmStrategyId, FarmStrategyLoot loot);
    public List<FarmStrategy> getAllOrderedByProfit();
    public List<FarmStrategy> getAllOrderedBySafeProfit();
    public FarmStrategy getById(final FarmStrategyId farmStrategyId);
    public FarmStrategy updateProfits(final FarmStrategyId farmStrategyId, final long timestamp,
            final Currency medianSalePrice, final Currency minSalePrice,final int salePriceSampleSize);
    public int size();
    public FarmStrategy initProfits(final FarmStrategyId farmStrategyId);
    public void delete(final FarmStrategyId farmStrategyId);
}
