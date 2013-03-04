package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.SaleStrategyId;

public interface SaleStrategyMaterialDAO extends Serializable {
    public SaleStrategyMaterial insert(final SaleStrategyMaterial saleStrategyMaterial);
    public void delete(final SaleStrategyMaterial saleStrategyMaterial);
    public void deleteAllBySaleStrategyId(final SaleStrategyId saleStrategyId);
    public SaleStrategyMaterial getById(final long id);
    public List<SaleStrategyMaterial> getAllBySaleStrategyId(final SaleStrategyId saleStrategyId);
    public int getSize();
}
