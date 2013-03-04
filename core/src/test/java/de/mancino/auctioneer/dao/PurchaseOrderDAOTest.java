package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.dao.PurchaseDAOTest.createPurchase;
import static de.mancino.auctioneer.dto.components.PurchaseOrderId.purchaseOrderId;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import de.mancino.auctioneer.ContextTest;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.Purchase;
import de.mancino.auctioneer.dto.PurchaseOrder;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;
import de.mancino.auctioneer.strategies.BuyoutStrategy;


public class PurchaseOrderDAOTest extends ContextTest {
    private static PurchaseOrderDAO objectUnderTest;

    @BeforeClass
    public static void initObjectUnderTest() throws ArmoryItemAlreadyExistingException {
        objectUnderTest = (PurchaseOrderDAO) context.getBean("purchaseOrderDAO");
    }

    public static PurchaseOrder createPurchaseOrder(final String itemName) {
        return createPurchaseOrder(ArmoryItemDAOTest.createArmoryItem(itemName));
    }

    public static PurchaseOrder createPurchaseOrder(final ArmoryItem armoryItem) {
        final PurchaseOrderId id = purchaseOrderId((int)nextId());
        final List<Purchase> purchases = new ArrayList<Purchase>();
        final int itemCount = RANDOM.nextInt(10);
        for(int i =0 ; i < itemCount ; i++) {
            purchases.add(createPurchase(id));
        }
        return createPurchaseOrder(true, armoryItem, id , RANDOM.nextInt(100)+1,
                createPrice(), BuyoutStrategy.values()[RANDOM.nextInt(BuyoutStrategy.values().length)],
                System.currentTimeMillis(), purchases);
    }

    public static PurchaseOrder createPurchaseOrder(boolean active, ArmoryItem armoryItem,
            PurchaseOrderId id, int itemsMissing, Currency maxPrice,
            BuyoutStrategy strategy, long timeInMilliseconds, List<Purchase> purchases) {
        final PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setActive(active);
        purchaseOrder.setArmoryItem(armoryItem);
        purchaseOrder.setId(id);
        purchaseOrder.setMaxPrice(maxPrice);
        purchaseOrder.setPurchases(purchases);
        purchaseOrder.setItemCount(itemsMissing + purchaseOrder.getItemCount());
        purchaseOrder.setStrategy(strategy);
        purchaseOrder.setTimeInMilliseconds(timeInMilliseconds);
        return purchaseOrder;
    }

    public static void comparePurchaseOrder(final PurchaseOrder expected, final PurchaseOrder actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getItemCount(), actual.getItemCount());
        assertEquals(expected.getMaxPrice().toLong(), actual.getMaxPrice().toLong());
        assertEquals(expected.getStrategy(), actual.getStrategy());
        assertEquals(expected.getTimeInMilliseconds(), actual.getTimeInMilliseconds());
    }


    @Test
    public void testInsert() {
        final PurchaseOrder created = createPurchaseOrder("testInsert");

        final PurchaseOrder returned = objectUnderTest.insert(created);
        // change created-id by hand
        created.setId(returned.getId());
        comparePurchaseOrder(created, returned);

        final PurchaseOrder fetched = objectUnderTest.getByPurchaseOrderId(returned.getId());
        comparePurchaseOrder(returned, fetched);
    }

    @Test
    public void testSize() {
        final int baseSize = objectUnderTest.getSize();
        final int addedItems = 250;
        for(int i = 0 ; i < addedItems ; i++) {
            objectUnderTest.insert(createPurchaseOrder("Item_" + i));
        }
        assertEquals(baseSize+addedItems, objectUnderTest.getSize());
    }

    @Test
    public void testDeleteByOrderId() {
        final PurchaseOrder created = createPurchaseOrder("testInsert");
        final PurchaseOrder returned = objectUnderTest.insert(created);
        final PurchaseOrder fetched = objectUnderTest.getByPurchaseOrderId(returned.getId());

        objectUnderTest.delete(fetched);
        try {
            objectUnderTest.getByPurchaseOrderId(returned.getId());
            fail("Object shouldn't exist - an Exception should by thrown!");
        } catch (EmptyResultDataAccessException e) {
            // OK!
        }
    }
}
