package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.components.PriceWatchId;

public interface PriceSampleDAO extends Serializable {
    public PriceSample insert(final PriceSample priceSample);

    public void delete(final PriceSample priceSample);

    public void deleteAllByPriceWatchId(final PriceWatchId priceWatchId);
    
    public void deleteAllByMaxTimestamp(final long maxTimestamp);

    public PriceSample getById(final long id);

    public List<PriceSample> getAllByPriceWatchId(final PriceWatchId priceWatchId);

    public int getSize();
}
