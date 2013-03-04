package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.CashSample;
import de.mancino.auctioneer.dto.components.CharacterId;

public interface CashSampleDAO extends Serializable {
    public CashSample insert(final CashSample priceSample);

    public void delete(final CashSample priceSample);

    public void deleteAllByCharacterId(final CharacterId characterId);
    
    public void deleteAllByMaxTimestamp(final long maxTimestamp);

    public CashSample getById(final long id);

    public List<CashSample> getAllByCharacterId(final CharacterId characterId);

    public int getSize();
}
