package de.mancino.auctioneer.strategies;

import java.util.List;

import org.junit.Test;

import de.mancino.armory.json.api.auction.Auction;
import de.mancino.auctioneer.dto.PurchaseOrder;

public class BuyoutCheapestFirstTest extends BuyoutStrategyTest {
    private final Auction AI_5x100 = item(5, 100);
    private final Auction AI_1x50 = item(1, 50);
    private final Auction AI_50x29 = item(50, 29);
    private final Auction AI_5x30 = item(5, 30);
    private final Auction AI_5x1 = item(5, 1);
    private final Auction AI_20x1 = item(20, 1);
    private final BuyoutCheapestFirst objectUnderTest = (BuyoutCheapestFirst) BuyoutStrategy.CHEAPEST_FIRST.strategy;

    @Test
    public void testOrder() {
        List<Auction> originalItems = itemList(AI_5x100, AI_1x50, AI_50x29, AI_5x30, AI_5x1, AI_20x1);
        List<Auction> expectedOrder = itemList(AI_20x1, AI_5x1, AI_50x29, AI_5x30, AI_1x50, AI_5x100);
        List<Auction> sortedItems = objectUnderTest.sortByCheapestAndBiggest(originalItems);
        compareLists(originalItems, sortedItems, expectedOrder);
    }

    @Test
    public void testFilterEnoughSpace() {
        List<Auction> originalItems = itemList(AI_5x100, AI_1x50, AI_50x29, AI_5x30, AI_5x1, AI_20x1);
        List<Auction> expectedItems = itemList(AI_20x1, AI_5x1, AI_50x29);
        PurchaseOrder purchasingOrder = order(1000, 29);
        checkStrategy(originalItems, objectUnderTest, purchasingOrder, expectedItems);
    }

    @Test
    public void testFilterLimitedSpace() {
        List<Auction> originalItems = itemList(AI_5x100, AI_1x50, AI_50x29, AI_5x30, AI_5x1, AI_20x1);
        List<Auction> expectedItems = itemList(AI_20x1, AI_5x1, AI_5x30);
        PurchaseOrder purchasingOrder = order(31, 30);
        checkStrategy(originalItems, objectUnderTest, purchasingOrder, expectedItems);
    }
    @Test
    public void testIgnoreInactive() {
        List<Auction> originalItems = itemList(AI_5x100, AI_1x50, AI_50x29, AI_5x30, AI_5x1, AI_20x1);
        List<Auction> expectedItems = itemList();
        PurchaseOrder purchasingOrder = order(10000, 10000, false);
        checkStrategy(originalItems, objectUnderTest, purchasingOrder, expectedItems);
    }
}
