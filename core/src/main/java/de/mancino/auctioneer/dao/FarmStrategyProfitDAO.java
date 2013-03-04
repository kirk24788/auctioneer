package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.FarmStrategyProfit;
import de.mancino.auctioneer.dto.components.FarmStrategyId;

public interface FarmStrategyProfitDAO extends Serializable {
    public FarmStrategyProfit insert(final FarmStrategyProfit farmStrategyProfit);
    public void delete(final FarmStrategyProfit farmStrategyProfit);
    public void deleteAllByFarmStrategyId(final FarmStrategyId farmStrategyId);
    public void deleteAllByMaxTimestamp(final long maxTimestamp);
    public FarmStrategyProfit getById(final long id);
    public List<FarmStrategyProfit> getAllByFarmStrategyId(final FarmStrategyId farmStrategyId);
    public int getSize();
}
