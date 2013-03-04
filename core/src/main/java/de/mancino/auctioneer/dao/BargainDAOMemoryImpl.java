package de.mancino.auctioneer.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.exceptions.BargainDoesnNotExistException;

public class BargainDAOMemoryImpl implements BargainDAO {
    private static final long serialVersionUID = 1L;
    private List<Bargain> deals = new ArrayList<>();

    private int getNextId() {
        int nextId = 1;
        for(final Bargain deal : deals) {
            if(nextId <= deal.getId()) {
                nextId = deal.getId()+1;
            }
        }
        return nextId;
    }

    @Override
    public Bargain insert(final Bargain deal) {
        synchronized (deals) {
            deals.add(deal);
            deal.setId(getNextId());
            return deal;
        }
    }

    @Override
    public void delete(Bargain deal) {
        synchronized (deals) {
            deals.remove(deal);
        }
    }

    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        final List<Bargain> deleteList = new ArrayList<>();
        synchronized (deals) {
            for(final Bargain deal : deals) {
                if(deal.getTimestamp()<maxTimestamp) {
                    deleteList.add(deal);
                }
            }
            deals.removeAll(deleteList);
        }
    }

    @Override
    public Bargain getById(int id) throws BargainDoesnNotExistException {
        synchronized (deals) {
            for(final Bargain deal : deals) {
                if(deal.getId()==id) {
                    return deal;
                }
            }
            throw new BargainDoesnNotExistException(id);
        }
    }

    @Override
    public List<Bargain> getAll() {
        synchronized (deals) {
            return Collections.unmodifiableList(deals);
        }
    }

    @Override
    public int getSize() {
        synchronized (deals) {
            return deals.size();
        }
    }
}
