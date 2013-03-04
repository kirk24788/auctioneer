package de.mancino.auctioneer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.components.PriceWatchId;

public class PriceWatch implements Serializable {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2L;

    private PriceWatchId id;
    private ArmoryItem armoryItem;
    private boolean highlighted;
    private List<PriceSample> priceSamples;

    protected PriceWatch() {
        this(null);
    }

    public PriceWatch(final ArmoryItem armoryItem) {
        this.priceSamples = new ArrayList<PriceSample>();
        this.setArmoryItem(armoryItem);
    }

    /**
     * @return the id
     */
    public PriceWatchId getId() {
        return id;
    }

    /**
     * @return the priceSamples
     */
    public List<PriceSample> getPriceSamples() {
        return priceSamples==null ? null : Collections.unmodifiableList(priceSamples);
    }
    
    public void setPriceSamples(List<PriceSample> priceSamples) {
        this.priceSamples = priceSamples;
    }

    /**
     * @return the amoryItem
     */
    public ArmoryItem getArmoryItem() {
        return armoryItem;
    }

    /**
     * @param id the id to set
     */
    public void setId(PriceWatchId id) {
        this.id = id;
    }

    /**
     * @param armoryItem the armoryItem to set
     */
    public void setArmoryItem(ArmoryItem armoryItem) {
        this.armoryItem = armoryItem;
    }

    /**
     * @return the highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @param highlighted the highlighted to set
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}

