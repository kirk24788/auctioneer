package de.mancino.auctioneer.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.mancino.armory.json.api.auction.Auction;
import de.mancino.auctioneer.dto.PurchaseOrder;

public class BuyoutWithDoubleThreshold implements IBuyoutStrategy {
    @Override
    public List<Auction> filterCandidates(final List<Auction> allAuctions, final PurchaseOrder purchasingOrder) {
        final List<Auction> sortedAuctions = sortByCheapestAndBiggest(allAuctions);
        final List<Auction> auctionCandidates = new ArrayList<Auction>();
        if(!purchasingOrder.isActive()) {
            long slotsLeft = purchasingOrder.getItemCount() - purchasingOrder.getItemsBought();
            long thresholdLeft = purchasingOrder.getItemCount() + slotsLeft;
            for(final Auction item : sortedAuctions) {
                if(slotsLeft > 0 && item.quantity <= thresholdLeft) {
                    auctionCandidates.add(item);
                    slotsLeft -= item.quantity;
                    thresholdLeft -= item.quantity;
                }
            }
        }
        return auctionCandidates;
    }
    
    List<Auction> sortByCheapestAndBiggest(final List<Auction> allAuctions) {
        List<Auction> allAuctionsCopy = new ArrayList<Auction>(allAuctions);
        Collections.sort(allAuctionsCopy, new Comparator<Auction>() {
            /**
             * @param o1 the first object to be compared.
             * @param o2 the second object to be compared.
             * @return a negative integer, zero, or a positive integer as the first argument is less than, 
             * equal to, or greater than the second.
             */ 
            @Override
            public int compare(Auction o1, Auction o2) {
                long o1PPI = o1.buyout / o1.quantity;
                long o2PPI = o2.buyout / o2.quantity;
                if (o1PPI < o2PPI) {
                    return -1;
                } else if (o1PPI == o2PPI) {
                    return o2.quantity - o1.quantity;
                } else {
                    return 1;
                }
            }
        });
        return allAuctionsCopy;
    }
}
