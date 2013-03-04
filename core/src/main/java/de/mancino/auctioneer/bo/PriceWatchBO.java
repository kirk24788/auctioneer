package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PriceWatchId;
import de.mancino.auctioneer.exceptions.PriceWatchAlreadyExistingException;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

public interface PriceWatchBO extends Serializable {
    public PriceWatch createPriceWatch(final ArmoryItem armoryItem) throws PriceWatchAlreadyExistingException;
    public void addPriceSample(final PriceWatchId priceWatchId, final List<Currency> pricesList, final long timestamp);
    public void deletePriceWatch(final PriceWatch priceWatch);
    public PriceWatch findByArmoryId(final ArmoryId armoryId) throws PriceWatchDoesnNotExistException;
    public List<PriceWatch> listPriceWatches();
    public void deleteOldPrices(final long maxTimestamp);
    public void setHighlighted(PriceWatch priceWatch, boolean highlighted);
    public List<PriceWatch> listAllHighlighted();
}