package de.mancino.auctioneer.strategies;

import static de.mancino.auctioneer.dto.components.Currency.currency;
import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import de.mancino.armory.json.api.auction.Auction;
import de.mancino.auctioneer.dto.PurchaseOrder;

public class BuyoutStrategyTest {

    protected static Auction item(final int count, final long price) {
        Auction item = new Auction();
        item.buyout = price * count;
        item.quantity = count;
        return item;
    }
    protected static List<Auction> itemList(final Auction ... items) {
        List<Auction> list = new ArrayList<Auction>();
        for(Auction item : items) {
            list.add(item);
        }
        return list;
    }

    protected static PurchaseOrder order(final int totalCount, final long maxPrice) {
        return order(totalCount, maxPrice, true);
    }

    protected static PurchaseOrder order(final int totalCount, final long maxPrice,
            final boolean isActive) {
        PurchaseOrder purchasingOrder = new PurchaseOrder();
        purchasingOrder.setItemCount(totalCount);
        purchasingOrder.setMaxPrice(currency(maxPrice));
        purchasingOrder.setActive(isActive);
        return purchasingOrder;
    }

    protected void checkStrategy(final List<Auction> originalItems, final IBuyoutStrategy bs,
            final PurchaseOrder purchasingOrder, final List<Auction> expectedItems) {
        final List<Auction> sortedItems = bs.filterCandidates(originalItems, purchasingOrder);
        compareLists(originalItems, sortedItems, expectedItems);
    }
    protected void compareLists(final List<Auction> originalItems, final List<Auction> sortedItems,
            final List<Auction> expectedItems) {
        if(originalItems == sortedItems) {
            throw new AssertionError("Both Lists are the same! Never change a List itself, always make copies!");
        }
        if( expectedItems.size() != sortedItems.size() ) {
            throw new AssertionError("Lists differ in Size! "
                    + "\n Expected " + expectedItems.size() + ": " + itemListToString(expectedItems)
                    + "\n      Got: " + sortedItems.size() + "" + itemListToString(sortedItems));
        }
        assertEquals("Sorted List Size doesn't match expected List Size", expectedItems.size(), sortedItems.size());
        for(int i = 0 ; i < expectedItems.size() ; i++) {
            if(sortedItems.get(i) != expectedItems.get(i) ) {
                throw new AssertionError("Lists differ at Index " + i
                        + "\n Expected: " + itemListToString(expectedItems)
                        + "\n      Got: " + itemListToString(sortedItems));
            }
        }
    }

    private String itemListToString(final List<Auction> items) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(final de.mancino.armory.json.api.auction.Auction item : items) {
            sb.append(item.quantity).append("@").append(item.buyout/item.quantity).append("=").append(item.buyout).append("; ");
        }
        sb.append("]");
        return sb.toString();
    }
}
