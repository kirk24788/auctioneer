package de.mancino.auctioneer.dto;

import static de.mancino.auctioneer.dto.components.Currency.currency;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.SaleStrategyId;

public class SaleStrategyMargin  implements Comparable<SaleStrategyMargin>, Serializable {
    /**
     * Serial Version UID
     */
    public static final long serialVersionUID = 1L;

    private long id;

    private SaleStrategyId saleStrategyId;

    private long marginTimestamp;
    private Currency medianMaterialCost;
    private Currency minMaterialCost;
    private int materialCostSampleSize;
    private Currency medianSalePrice;
    private Currency minSalePrice;
    private int salePriceSampleSize;

    public SaleStrategyMargin(final SaleStrategyId saleStrategyId) {
        this(saleStrategyId, 0L, currency(0),currency(0),0,currency(0),currency(0),0);
    }

    public SaleStrategyMargin(final SaleStrategyId saleStrategyId, final long marginTimestamp,
            final Currency medianMaterialCost, final Currency minMaterialCost, final int materialCostSampleSize,
            final Currency medianSalePrice, final Currency minSalePrice,final int salePriceSampleSize) {
        this.saleStrategyId = saleStrategyId;
        this.marginTimestamp = marginTimestamp;
        this.materialCostSampleSize = materialCostSampleSize;
        this.minMaterialCost = minMaterialCost;
        this.medianMaterialCost = medianMaterialCost;
        this.salePriceSampleSize = salePriceSampleSize;
        this.minSalePrice = minSalePrice;
        this.medianSalePrice = medianSalePrice;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("COST: [").append(minMaterialCost).append(" / ").append(medianMaterialCost);
        sb.append("] SALE PRICE: [").append(minSalePrice).append(" / ").append(medianSalePrice);
        sb.append("] PROFIT: ").append(getProfit());
        return sb.toString();
    }


    /**
     * @return the profit
     */
    public Currency getProfit() {
        return getProfit(currency(0));
    }

    /**
     * @return the profit
     */
    public Currency getProfit(final Currency additionalExpenses) {
        return new Currency(minSalePrice.toLong() - minMaterialCost.toLong() - additionalExpenses.toLong());
    }

    /**
     * @return the profit
     */
    public Currency getSafeProfit() {
        return getSafeProfit(currency(0));
    }

    /**
     * @return the profit
     */
    public Currency getSafeProfit(final Currency additionalExpenses) {
        return new Currency(minSalePrice.toLong() - medianMaterialCost.toLong() - additionalExpenses.toLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SaleStrategyMargin o) {
        final long profitDifference = (o.getProfit().toLong() - getProfit().toLong());
        if (profitDifference > 0) {
            return 1;
        } else if (profitDifference < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * @return the medianMaterialCost
     */
    public Currency getMedianMaterialCost() {
        return medianMaterialCost;
    }

    /**
     * @param medianMaterialCost the medianMaterialCost to set
     */
    public void setMedianMaterialCost(Currency medianMaterialCost) {
        this.medianMaterialCost = medianMaterialCost;
    }

    /**
     * @return the materialCostSampleSize
     */
    public int getMaterialCostSampleSize() {
        return materialCostSampleSize;
    }

    /**
     * @param materialCostSampleSize the materialCostSampleSize to set
     */
    public void setMaterialCostSampleSize(int materialCostSampleSize) {
        this.materialCostSampleSize = materialCostSampleSize;
    }

    /**
     * @return the salePriceSampleSize
     */
    public int getSalePriceSampleSize() {
        return salePriceSampleSize;
    }

    /**
     * @param salePriceSampleSize the salePriceSampleSize to set
     */
    public void setSalePriceSampleSize(int salePriceSampleSize) {
        this.salePriceSampleSize = salePriceSampleSize;
    }

    /**
     * @return the saleStrategyId
     */
    public SaleStrategyId getSaleStrategyId() {
        return saleStrategyId;
    }

    /**
     * @param saleStrategyId the saleStrategyId to set
     */
    public void setSaleStrategyId(SaleStrategyId saleStrategyId) {
        this.saleStrategyId = saleStrategyId;
    }


    /**
     * @return the marginTimestamp
     */
    public long getMarginTimestamp() {
        return marginTimestamp;
    }

    /**
     * @param marginTimestamp the marginTimestamp to set
     */
    public void setMarginTimestamp(long marginTimestamp) {
        this.marginTimestamp = marginTimestamp;
    }

    /**
     * @return the minSalePrice
     */
    public Currency getMinSalePrice() {
        return minSalePrice;
    }

    /**
     * @param minSalePrice the minSalePrice to set
     */
    public void setMinSalePrice(Currency minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    /**
     * @return the minMaterialCost
     */
    public Currency getMinMaterialCost() {
        return minMaterialCost;
    }

    /**
     * @param minMaterialCost the minMaterialCost to set
     */
    public void setMinMaterialCost(Currency minMaterialCost) {
        this.minMaterialCost = minMaterialCost;
    }

    /**
     * @return the medianSalePrice
     */
    public Currency getMedianSalePrice() {
        return medianSalePrice;
    }

    /**
     * @param medianSalePrice the medianSalePrice to set
     */
    public void setMedianSalePrice(Currency medianSalePrice) {
        this.medianSalePrice = medianSalePrice;
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
