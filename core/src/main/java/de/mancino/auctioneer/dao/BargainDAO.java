package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.exceptions.BargainDoesnNotExistException;

public interface BargainDAO extends Serializable {
    public Bargain insert(final Bargain deal);
    public void delete(final Bargain deal);
    public void deleteAllByMaxTimestamp(final long maxTimestamp);
    public Bargain getById(final int id) throws BargainDoesnNotExistException;
    public List<Bargain> getAll();
    public int getSize();
}
