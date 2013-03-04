package de.mancino.auctioneer.dto;

import static de.mancino.auctioneer.dto.components.Currency.currency;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.FarmStrategyId;

public class FarmStrategyProfit  implements Comparable<FarmStrategyProfit>, Serializable {
    /**
     * Serial Version UID
     */
    public static final long serialVersionUID = 1L;

    private long id;

    private FarmStrategyId farmStrategyId;
    private long profitTimestamp;
    private Currency medianSalePrice;
    private Currency minSalePrice;
    private int salePriceSampleSize;

    public FarmStrategyProfit(final FarmStrategyId FarmStrategyId) {
        this(FarmStrategyId, 0L, currency(0),currency(0),0);
    }

    public FarmStrategyProfit(final FarmStrategyId farmStrategyId, final long profitTimestamp,
            final Currency medianSalePrice, final Currency minSalePrice,final int salePriceSampleSize) {
        this.setFarmStrategyId(farmStrategyId);
        this.setProfitTimestamp(profitTimestamp);
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
        sb.append("SALE-PRICE: ").append(minSalePrice).append(" - ").append(medianSalePrice);
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(FarmStrategyProfit o) {
        final long profitDifference = (o.minSalePrice.toLong() - minSalePrice.toLong());
        if (profitDifference > 0) {
            return 1;
        } else if (profitDifference < 0) {
            return -1;
        } else {
            return 0;
        }
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

    /**
     * @return the profitTimestamp
     */
    public long getProfitTimestamp() {
        return profitTimestamp;
    }

    /**
     * @param profitTimestamp the profitTimestamp to set
     */
    public void setProfitTimestamp(long profitTimestamp) {
        this.profitTimestamp = profitTimestamp;
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
