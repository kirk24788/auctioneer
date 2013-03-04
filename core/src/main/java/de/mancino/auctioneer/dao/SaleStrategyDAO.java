package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.components.SaleStrategyId;

public interface SaleStrategyDAO extends Serializable {
    public SaleStrategy insert(final SaleStrategy saleStrategy);
    public void delete(final SaleStrategyId saleStrategyId);
    public List<SaleStrategy> getAll();
    public SaleStrategy getBySaleStrategyId(final SaleStrategyId saleStrategyId);
    public int getSize();
    public void update(final SaleStrategy saleStrategy);
}

