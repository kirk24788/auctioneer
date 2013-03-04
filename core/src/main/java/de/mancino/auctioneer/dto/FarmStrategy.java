package de.mancino.auctioneer.dto;

import static de.mancino.auctioneer.dto.components.Currency.currency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.FarmStrategyId;

public class FarmStrategy implements Comparable<FarmStrategy>, Serializable {
    /**
     * Serial Version UID
     */
    public static final long serialVersionUID = 1L;

    private FarmStrategyId id;
    private List<FarmStrategyLoot> loot;
    private List<FarmStrategyProfit> profits;
    private ArmoryItem iconItem;
    private Currency additionalProfits;
    private String strategyName;

    public FarmStrategy(final String strategyName, final ArmoryItem iconItem, final Currency additionalProfits) {
        this(strategyName, iconItem,  additionalProfits, new ArrayList<FarmStrategyLoot>());
    }

    public FarmStrategy(final String strategyName, final ArmoryItem iconItem, final Currency additionalProfits,
            List<FarmStrategyLoot> loot) {
        this.setStrategyName(strategyName);
        this.loot = loot;
        this.iconItem = iconItem;
        this.additionalProfits = additionalProfits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FARM STRATEGY [ID:").append(id).append("; NAME:").append(getStrategyName())
        .append("] YIELDS [");
        for (final FarmStrategyLoot l : getLoot()) {
            sb.append(l.getItemCount()).append(" OF ").append(l.getItem())
            .append(";");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("] PROFIT: ").append(getTotalProfit());
        return sb.toString();
    }

    /**
     * @return the profit
     */
    public Currency getTotalSafeProfit() {
        return  new Currency(getLatestProfit().getMinSalePrice().toLong() + additionalProfits.toLong());
    }

    /**
     * @return the profit
     */
    public Currency getTotalProfit() {
        return  new Currency(getLatestProfit().getMedianSalePrice().toLong() + additionalProfits.toLong());
    }

    public FarmStrategyProfit getLatestProfit() {
        if(getProfits().size()>0) {
            return getProfits().get(getProfits().size()-1);
        } else {
            return new FarmStrategyProfit(id,0, currency(0), currency(0), 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(FarmStrategy o) {
        final long profitDifference = (o.getTotalProfit().toLong() - getTotalProfit()
                .toLong());
        if (profitDifference > 0) {
            return 1;
        } else if (profitDifference < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public FarmStrategyId getId() {
        return id;
    }

    public void setId(FarmStrategyId id) {
        this.id = id;
    }

    public List<FarmStrategyLoot> getLoot() {
        return loot != null ? Collections.unmodifiableList(loot) : new ArrayList<FarmStrategyLoot>();
    }

    public void setLoot(List<FarmStrategyLoot> loot) {
        this.loot = loot;
    }

    public ArmoryItem getIconItem() {
        return iconItem;
    }

    public void setIconItem(ArmoryItem iconItem) {
        this.iconItem = iconItem;
    }

    public Currency getAdditionalProfits() {
        return additionalProfits;
    }

    public void setAdditionalProfits(Currency additionalProfits) {
        this.additionalProfits = additionalProfits;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    /**
     * @return the profits
     */
    public List<FarmStrategyProfit> getProfits() {
        return profits != null ? Collections.unmodifiableList(profits) : new ArrayList<FarmStrategyProfit>();
    }

    /**
     * @param profits the profits to set
     */
    public void setProfits(List<FarmStrategyProfit> profits) {
        this.profits = profits;
    }
}
