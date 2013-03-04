/*
 * GenericPrice.java 13.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.dto;

import java.io.Serializable;

import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.Currency;

public class CashSample implements Comparable<CashSample>, Serializable {
    /**
     * Serial Version UID
     */
    public static final long serialVersionUID = 1L;

    private long id;
    private long timeInMilliseconds;
    private Currency cash;
    private CharacterId characterId;


    public CashSample(final CharacterId characterId, final long timestamp, final Currency cash) {
        this.timeInMilliseconds = timestamp;
        this.cash = cash;
        this.characterId = characterId;
    }


    @Override
    public int compareTo(CashSample o) {
        return (int) (timeInMilliseconds-o.timeInMilliseconds);
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
     * @return the cash
     */
    public Currency getCash() {
        return cash;
    }

    /**
     * @param cash the cash to set
     */
    public void setCash(Currency cash) {
        this.cash = cash;
    }

    /**
     * @return the priceWatchId
     */
    public CharacterId getCharacterId() {
        return characterId;
    }

    /**
     * @param priceWatchId the priceWatchId to set
     */
    public void setCharacterId(CharacterId characterId) {
        this.characterId = characterId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CashSample[");
        sb.append("id=").append(id).append("; ");
        sb.append("characterId=").append(characterId).append("; ");
        sb.append("timeInMilliseconds=").append(timeInMilliseconds);
        sb.append("]");
        return sb.toString();
    }
}

