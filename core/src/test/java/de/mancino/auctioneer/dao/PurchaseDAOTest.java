package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.dto.components.PurchaseOrderId.purchaseOrderId;
import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.mancino.auctioneer.ContextTest;
import de.mancino.auctioneer.dto.Purchase;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;

public class PurchaseDAOTest extends ContextTest {
    public static final String DEFAULT_BUYER_NAME = "Buyer_1";
    private static PurchaseDAO objectUnderTest;

    @BeforeClass
    public static void initObjectUnderTest() throws ArmoryItemAlreadyExistingException {
        objectUnderTest = (PurchaseDAO) context.getBean("purchaseDAO");
    }

    public static Purchase createPurchase() {
        return createPurchase(purchaseOrderId(RANDOM.nextInt(100)+1));
    }

    public static Purchase createPurchase(final PurchaseOrderId purchaseOrderId) {
        return createPurchase(DEFAULT_BUYER_NAME, RANDOM.nextInt(50)+1,
                createPrice(),
                purchaseOrderId, System.currentTimeMillis());
    }

    public static Purchase createPurchase(final String buyerName, final int itemCount, final Currency price,
            final PurchaseOrderId purchaseOrderId, final long timeInMilliseconds) {
        final Purchase purchase = new Purchase();
        purchase.setBuyerName(buyerName);
        purchase.setItemCount(itemCount);
        purchase.setPrice(price);
        purchase.setPurchaseOrderId(purchaseOrderId);
        purchase.setTimeInMilliseconds(timeInMilliseconds);
        return purchase;
    }

    public static void comparePurchase(final Purchase expected, final Purchase actual) {
        assertEquals(expected.getBuyerName(), actual.getBuyerName());
        assertEquals(expected.getItemCount(), actual.getItemCount());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getPurchaseOrderId().toInt(), actual.getPurchaseOrderId().toInt());
        assertEquals(expected.getTimeInMilliseconds(), actual.getTimeInMilliseconds());
    }

    @Test
    public void testInsert() {
        final PurchaseOrderId poId = purchaseOrderId(120);
        final Purchase created = createPurchase(poId);

        objectUnderTest.insert(created);
        assertEquals(1, objectUnderTest.getAllByPurchaseOrderId(poId).size());

        final Purchase fetched = objectUnderTest.getAllByPurchaseOrderId(poId).get(0);
        comparePurchase(created, fetched);
    }

    @Test
    public void testSize() {
        final int baseSize = objectUnderTest.getSize();
        final int addedItems = 250;
        for(int i = 0 ; i < addedItems ; i++) {
            objectUnderTest.insert(createPurchase());
        }
        assertEquals(baseSize+addedItems, objectUnderTest.getSize());
    }

    @Test
    public void testDeleteByOrderId() {
        final PurchaseOrderId poId = purchaseOrderId(240);
        final int addedItems = 25;
        for(int i = 0 ; i < addedItems ; i++) {
            objectUnderTest.insert(createPurchase(poId));
        }
        assertEquals(addedItems, objectUnderTest.getAllByPurchaseOrderId(poId).size());
        objectUnderTest.deleteAllByPurchaseOrderId(poId);
        assertEquals(0, objectUnderTest.getAllByPurchaseOrderId(poId).size());
    }
}
