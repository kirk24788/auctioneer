package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;

public interface PriceWatchDAO extends Serializable {
    public PriceWatch insert(final PriceWatch priceWatch);
    public void delete(final PriceWatch priceSample);
    public List<PriceWatch> getAll();
    public PriceWatch getByArmoryId(final ArmoryId armoryId);
    public int getSize();
    public int getSize(final boolean highlighted);
    public void update(final PriceWatch priceWatch);
    public List<PriceWatch> getAllByHighlighted(final boolean highlighted);
}

