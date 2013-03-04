package de.mancino.auctioneer.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.mancino.armory.json.api.auction.Auction;
import de.mancino.auctioneer.dto.PurchaseOrder;

/**
 * Strategie, die die billigsten Items zuerst kauft und dann
 * passend auffüllt.
 *
 * ACHTUNG: Dies bedeutet NICHT, dass nur die billigtens Items gekauft werden!
 *
 * Beispiel:
 *  - Benötigt: 10 Items fur max. 5 G
 *  - Im AH: 1 Item für 1S
 *           11 Items für 1G
 *           9 Items für 4 G
 *           10 Items für 5G
 *
 *  - Gekauft: 1 Item für 99S
 *             9 Items für 4G
 *             = 37G
 *  - Billiger (aber mehr Items!):
 *             11 Items für 1G
 *             = 11G
 *
 *  Diese Strategie sollte entweder benutzt werden wenn das  Item Limit nicht ausgereizt wird
 *  oder exakt 1 ist!
 *
 * @author mmancino
 */
public class BuyoutCheapestFirst implements IBuyoutStrategy {
    @Override
    public List<Auction> filterCandidates(final List<Auction> allAuctions, final PurchaseOrder purchaseOrder) {
        final List<Auction> sortedAuctions = sortByCheapestAndBiggest(allAuctions);
        final List<Auction> auctionCandidates = new ArrayList<Auction>();
        if(purchaseOrder.isActive()) {
            int slotsLeft = purchaseOrder.getItemCount() - purchaseOrder.getItemsBought();
            for(final Auction item : sortedAuctions) {
                if(item.quantity <= slotsLeft) {
                    if(item.buyout <= (item.quantity * purchaseOrder.getMaxPrice().toLong())) {
                        auctionCandidates.add(item);
                        slotsLeft -= item.quantity;
                    }
                }
            }
        }
        return auctionCandidates;
    }

    List<Auction> sortByCheapestAndBiggest(final List<Auction> allAuctionItems) {
        List<Auction> allAuctionItemsCopy = new ArrayList<Auction>(allAuctionItems);
        Collections.sort(allAuctionItemsCopy, new Comparator<Auction>() {
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
        return allAuctionItemsCopy;
    }
}
