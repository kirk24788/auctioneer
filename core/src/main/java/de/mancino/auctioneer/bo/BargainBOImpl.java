package de.mancino.auctioneer.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.mancino.auctioneer.dao.BargainDAO;
import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.exceptions.BargainDoesnNotExistException;

public class BargainBOImpl implements BargainBO {
    private static final long serialVersionUID = 1L;

    private final BargainDAO dealDAO;

    public BargainBOImpl(final BargainDAO dealDAO) {
        this.dealDAO = dealDAO;
    }

    @Override
    public Bargain add(Bargain deal) {
        return dealDAO.insert(deal);
    }

    @Override
    public Bargain getById(int id) throws BargainDoesnNotExistException {
        return dealDAO.getById(id);
    }

    @Override
    public void removeAll() {
        dealDAO.deleteAllByMaxTimestamp(Long.MAX_VALUE);
    }

    @Override
    public List<Bargain> getAll() {
        return dealDAO.getAll();
    }

    @Override
    public List<Bargain> getAllOrderedByProfit() {
        final List<Bargain> sorted = new ArrayList<>(dealDAO.getAll());
        Collections.sort(sorted, new Comparator<Bargain>() {
            @Override
            public int compare(Bargain o1, Bargain o2) {
                long v1 = o1.getTotalSafeProfit().toLong();
                long v2 = o2.getTotalSafeProfit().toLong();
                if(v1 > v2) {
                    return -1;
                } else if(v1 < v2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return Collections.unmodifiableList(sorted);
    }

    @Override
    public List<Bargain> getAllOrderedBySafeProfit() {
        final List<Bargain> sorted = new ArrayList<>(dealDAO.getAll());
        Collections.sort(sorted, new Comparator<Bargain>() {
            @Override
            public int compare(Bargain o1, Bargain o2) {
                long v1 = o1.getTotalProfit().toLong();
                long v2 = o2.getTotalProfit().toLong();
                if(v1 > v2) {
                    return -1;
                } else if(v1 < v2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return Collections.unmodifiableList(sorted);
    }

}
