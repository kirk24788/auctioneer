package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.exceptions.BargainDoesnNotExistException;

public interface BargainBO extends Serializable {
    public Bargain add(final Bargain deal);
    public Bargain getById(final int id) throws BargainDoesnNotExistException;
    public void removeAll();
    public List<Bargain> getAll();
    public List<Bargain> getAllOrderedBySafeProfit();
    public List<Bargain> getAllOrderedByProfit();
}
