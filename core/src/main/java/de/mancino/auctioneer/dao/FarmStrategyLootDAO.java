package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.components.FarmStrategyId;

public interface FarmStrategyLootDAO extends Serializable {
    public FarmStrategyLoot insert(final FarmStrategyLoot farmStrategyLoot);
    public void delete(final FarmStrategyLoot farmStrategyLoot);
    public void deleteAllByFarmStrategyId(final FarmStrategyId farmStrategyId);
    public FarmStrategyLoot getById(final long id);
    public List<FarmStrategyLoot> getAllByFarmStrategyId(final FarmStrategyId farmStrategyId);
    public int getSize();
}
