package de.mancino.auctioneer.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.mancino.auctioneer.dto.components.Currency;

@XmlRootElement(name = "deal")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Bargain {
    private int id;
    private long timestamp;
    private ArmoryItem item;
    private Currency thresholdPrice;
    private Currency equalizedMinimum;
    private Currency equalizedMedian;
    private int itemCount;
    private Currency priceTotal;

    public Bargain() {
        this(null);
    }

    public Bargain(final ArmoryItem item) {
        this.item = item;
        this.thresholdPrice = new Currency(0);
        this.equalizedMedian = new Currency(0);
        this.equalizedMedian = new Currency(0);
        this.priceTotal = new Currency(0);
        this.itemCount = 0;
    }

    public Currency getTotalProfit() {
        return new Currency(equalizedMedian.toLong()*itemCount-priceTotal.toLong());
    }

    public Currency getTotalSafeProfit() {
        return new Currency(equalizedMinimum.toLong()*itemCount-priceTotal.toLong());
    }

    public int getPercentagedProfit() {
        return percent(priceTotal.toLong(), equalizedMinimum.toLong()*itemCount);
    }

    public int getPercentagedSafeProfit() {
        return percent(priceTotal.toLong(), equalizedMedian.toLong()*itemCount);
    }

    private int percent(final long part, final long sum) {
        return (int) (part*100.0/sum);
    }

    public void addAuction(final int itemCount, final long buyout) {
        this.itemCount += itemCount;
        this.priceTotal = new Currency(priceTotal.toLong() + buyout);
    }

    public boolean isHotDeal() {
        return getPercentagedSafeProfit()<=50;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("DEAL[").append(id).append("]: ").append(getItem()).append(" FOR ").append(getPriceTotal()).append(" (")
        .append(itemCount).append(" ITEMS BELOW ").append(getThresholdPrice()).append(") EQUALIZED PRICES: ").append(equalizedMinimum).append(" TO ").append(equalizedMedian)
        .append(" PROFIT: ").append(getTotalSafeProfit()).append(" (").append(getPercentagedSafeProfit()).append("%) TO ")
        .append(getTotalProfit()).append(" (").append(getPercentagedProfit()).append("%);");
        return sb.toString();
    }

    @XmlElement
    public ArmoryItem getItem() {
        return item;
    }
    public void setItem(ArmoryItem item) {
        this.item = item;
    }
    @XmlElement
    public Currency getEqualizedMinimum() {
        return equalizedMinimum;
    }
    public void setEqualizedMinimum(Currency equalizedMinimum) {
        this.equalizedMinimum = equalizedMinimum;
    }
    @XmlElement
    public Currency getEqualizedMedian() {
        return equalizedMedian;
    }
    public void setEqualizedMedian(Currency equalizedMedian) {
        this.equalizedMedian = equalizedMedian;
    }
    @XmlElement
    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    @XmlElement
    public Currency getPriceTotal() {
        return priceTotal;
    }
    public void setPriceTotal(Currency priceTotal) {
        this.priceTotal = priceTotal;
    }

    /**
     * @return the thresholdPrice
     */
    @XmlElement
    public Currency getThresholdPrice() {
        return thresholdPrice;
    }

    /**
     * @param thresholdPrice the thresholdPrice to set
     */
    public void setThresholdPrice(Currency thresholdPrice) {
        this.thresholdPrice = thresholdPrice;
    }

    /**
     * @return the timestamp
     */
    @XmlElement
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the id
     */
    @XmlElement
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
