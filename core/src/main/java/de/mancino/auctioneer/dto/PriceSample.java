/*
 * GenericPrice.java 13.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PriceWatchId;

public class PriceSample implements Comparable<PriceSample>, Serializable {
    /**
     * Serial Version UID
     */
    public static final long serialVersionUID = 1L;

    private long id;
    private long timeInMilliseconds;
    private Currency minimumPrice;
    private Currency averagePrice;
    private Currency medianPrice;
    private int sampleSize;
    private PriceWatchId priceWatchId;


    public PriceSample(final PriceWatchId priceWatchId, final long timestamp) {
        this(priceWatchId, new ArrayList<Currency>(), timestamp);
    }

    public PriceSample(final PriceWatchId priceWatchId, final List<Currency> pricesList, final long timestamp) {
        if(pricesList.size()>0) {
            minimumPrice = minimum(pricesList);
            medianPrice = median(pricesList);
            averagePrice = average(pricesList);
            sampleSize = pricesList.size();
        } else {
            minimumPrice = new Currency(0);
            medianPrice = new Currency(0);
            averagePrice = new Currency(0);
            sampleSize = 0;
        }
        this.priceWatchId = priceWatchId;
        this.timeInMilliseconds = timestamp;
    }


    @Override
    public int compareTo(PriceSample o) {
        return (int) (timeInMilliseconds-o.timeInMilliseconds);
    }

    private Currency median(final List<Currency> prices) {
        Collections.sort(prices);
        final int listSize = prices.size();
        if(listSize%2==1) {
            return prices.get((listSize-1)/2);
        } else {
            return new Currency((prices.get(listSize/2).toLong() + prices.get((listSize/2)-1).toLong())/2);
        }
    }

    private Currency minimum(final List<Currency> prices) {
        Collections.sort(prices);
        return prices.get(0);
    }

    private Currency average(final List<Currency> prices) {
        double totals = 0.0;
        for(Currency price : prices) {
            totals += price.toLong();
        }
        return new Currency((long) (totals/prices.size()));
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
     * @return the timeInMilliseconds
     */
    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    /**
     * @param timeInMilliseconds the timeInMilliseconds to set
     */
    public void setTimeInMilliseconds(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    /**
     * @return the minimumPrice
     */
    public Currency getMinimumPrice() {
        return minimumPrice;
    }

    /**
     * @param minimumPrice the minimumPrice to set
     */
    public void setMinimumPrice(Currency minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    /**
     * @return the averagePrice
     */
    public Currency getAveragePrice() {
        return averagePrice;
    }

    /**
     * @param averagePrice the averagePrice to set
     */
    public void setAveragePrice(Currency averagePrice) {
        this.averagePrice = averagePrice;
    }

    /**
     * @return the medianPrice
     */
    public Currency getMedianPrice() {
        return medianPrice;
    }

    /**
     * @param medianPrice the medianPrice to set
     */
    public void setMedianPrice(Currency medianPrice) {
        this.medianPrice = medianPrice;
    }

    /**
     * @return the sampleSize
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * @param sampleSize the sampleSize to set
     */
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
     * @return the priceWatchId
     */
    public PriceWatchId getPriceWatchId() {
        return priceWatchId;
    }

    /**
     * @param priceWatchId the priceWatchId to set
     */
    public void setPriceWatchId(PriceWatchId priceWatchId) {
        this.priceWatchId = priceWatchId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PriceSample[");
        sb.append("id=").append(id).append("; ");
        sb.append("priceWatchId=").append(priceWatchId).append("; ");
        sb.append("timeInMilliseconds=").append(timeInMilliseconds).append("; ");
        sb.append("minimumPrice=").append(minimumPrice).append("; ");
        sb.append("averagePrice=").append(averagePrice).append("; ");
        sb.append("medianPrice=").append(medianPrice).append("; ");
        sb.append("sampleSize=").append(sampleSize);
        sb.append("]");
        return sb.toString();
    }
}

