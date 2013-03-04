package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMargin;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.SaleStrategyId;

public interface SaleStrategyBO extends Serializable {
    public SaleStrategy createSaleStrategy(final ArmoryItem product, final int productCount, Currency additionalExpenses,
            SaleStrategyMaterial ... saleStrategyMaterials);
    public SaleStrategy createSaleStrategy(final ArmoryItem product, final int productCount, Currency additionalExpenses);
    public SaleStrategy addSaleStrategyMaterials(final SaleStrategy saleStrategy, SaleStrategyMaterial ... saleStrategyMaterials);
    public List<SaleStrategy> getAllOrderedByProfit();
    public SaleStrategy getById(final SaleStrategyId saleStrategyId);
    public SaleStrategy updateMargins(final SaleStrategyId saleStrategyId, final long marginTimestamp,
            final Currency medianMaterialCost, final Currency minMaterialCost, final int materialCostSampleSize,
            final Currency medianSalePrice, final Currency minSalePrice,final int salePriceSampleSize);
    public int size();
    public SaleStrategy updateMargins(final SaleStrategyId saleStrategyId, final SaleStrategyMargin saleStrategyMargin);
    public void initMargins(final SaleStrategyId saleStrategyId);
    public List<SaleStrategy> getAllOrderedBySafeProfit();
    public void delete(final SaleStrategyId saleStrategyId);
}
