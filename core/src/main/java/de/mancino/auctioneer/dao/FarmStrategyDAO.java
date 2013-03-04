package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.components.FarmStrategyId;

public interface FarmStrategyDAO extends Serializable {
    public FarmStrategy insert(final FarmStrategy farmStrategy);
    public void delete(final FarmStrategyId farmStrategyId);
    public List<FarmStrategy> getAll();
    public FarmStrategy getById(final FarmStrategyId id);
    public int getSize();
    public void update(final FarmStrategy farmStrategy);
}

