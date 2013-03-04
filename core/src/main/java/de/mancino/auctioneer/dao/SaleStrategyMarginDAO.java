package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.SaleStrategyMargin;
import de.mancino.auctioneer.dto.components.SaleStrategyId;

public interface SaleStrategyMarginDAO extends Serializable {
    public SaleStrategyMargin insert(final SaleStrategyMargin saleStrategyMargin);

    public void delete(final SaleStrategyMargin saleStrategyMargin);

    public void deleteAllBySaleStrategyId(final SaleStrategyId saleStrategyId);

    public void deleteAllByMaxTimestamp(final long maxTimestamp);

    public SaleStrategyMargin getById(final long id);

    public List<SaleStrategyMargin> getAllBySaleStrategyId(final SaleStrategyId saleStrategyId);

    public int getSize();
}
