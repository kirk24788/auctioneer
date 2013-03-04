package de.mancino.auctioneer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.SaleStrategyId;

public class SaleStrategy  implements Comparable<SaleStrategy>, Serializable {
    /**
     * Serial Version UID
     */
    public static final long serialVersionUID = 1L;

    private SaleStrategyId id;
    private List<SaleStrategyMaterial> materials;
    private int productCount;
    private ArmoryItem product;

    private Currency additionalExpenses;

    private List<SaleStrategyMargin> margins;

    private long marginTimestamp;
    private Currency medianMaterialCost;
    private Currency minMaterialCost;
    private int materialCostSampleSize;
    private Currency medianSalePrice;
    private Currency minSalePrice;
    private int salePriceSampleSize;

    public SaleStrategy(final int productCount, final ArmoryItem product) {
        this(productCount, product, new ArrayList<SaleStrategyMaterial>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SALE STRATEGY [ID:").append(id).append("] ");
        sb.append(productCount).append(" OF ").append(getProduct()).append(" CREATED BY [");
        if(getMaterials() != null) {
            for(final SaleStrategyMaterial ssm : getMaterials()) {
                sb.append(ssm.getItemCount()).append(" OF ").append(ssm.getItem()).append(";");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("] COST: [").append(minMaterialCost).append(" / ").append(medianMaterialCost);
        sb.append("] SALE PRICE: [").append(minSalePrice).append(" / ").append(medianSalePrice);
        sb.append("] PROFIT: ").append(getProfit());
        return sb.toString();
    }

    public SaleStrategy(final int productCount, final ArmoryItem product, List<SaleStrategyMaterial> materials) {
        this.materials = materials;
        this.productCount = productCount;
        this.product = product;

        marginTimestamp = 0;
        additionalExpenses = new Currency(0);
        materialCostSampleSize = 0;
        minMaterialCost = new Currency(0);
        medianMaterialCost = new Currency(0);
        salePriceSampleSize = 0;
        minSalePrice = new Currency(0);
        medianSalePrice = new Currency(0);
    }

    /**
     * @return the profit
     */
    public Currency getSafeProfit() {
        return new Currency(productCount * minSalePrice.toLong() - medianMaterialCost.toLong() - additionalExpenses.toLong());
    }

    /**
     * @return the profit
     */
    public Currency getProfit() {
        return new Currency(productCount * minSalePrice.toLong() - minMaterialCost.toLong() - additionalExpenses.toLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SaleStrategy o) {
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
     * @return the id
     */
    public SaleStrategyId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(SaleStrategyId id) {
        this.id = id;
    }

    /**
     * @return the materials
     */
    public List<SaleStrategyMaterial> getMaterials() {
        return materials==null ? null : Collections.unmodifiableList(materials);
    }

    /**
     * @param materials the materials to set
     */
    public void setMaterials(List<SaleStrategyMaterial> materials) {
        this.materials = materials;
    }

    /**
     * @return the productCount
     */
    public int getProductCount() {
        return productCount;
    }

    /**
     * @param productCount the productCount to set
     */
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    /**
     * @return the product
     */
    public ArmoryItem getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(ArmoryItem product) {
        this.product = product;
    }

    /**
     * @return the additionalExpenses
     */
    public Currency getAdditionalExpenses() {
        return additionalExpenses;
    }

    /**
     * @param additionalExpenses the additionalExpenses to set
     */
    public void setAdditionalExpenses(Currency additionalExpenses) {
        this.additionalExpenses = additionalExpenses;
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
     * @return the margins
     */
    public List<SaleStrategyMargin> getMargins() {
        return margins==null ? null : Collections.unmodifiableList(margins);
    }

    /**
     * @param margins the margins to set
     */
    public void setMargins(List<SaleStrategyMargin> margins) {
        this.margins = margins;
    }
}
