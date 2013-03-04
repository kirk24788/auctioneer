package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.BeanGenerator.generatePriceSample;
import static de.mancino.auctioneer.dto.components.PriceWatchId.priceWatchId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import de.mancino.auctioneer.ContextTest;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.components.PriceWatchId;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;

public class PriceSampleDAOTest extends ContextTest {
    private static PriceSampleDAO objectUnderTest;

    @BeforeClass
    public static void initObjectUnderTest() throws ArmoryItemAlreadyExistingException {
        objectUnderTest = (PriceSampleDAO) context.getBean("priceSampleDAO");
    }

    @Test 
    public void testDoubleInsert() throws InterruptedException {
        final PriceSample item = generatePriceSample(priceWatchId(1));
        objectUnderTest.insert(item);
        objectUnderTest.insert(item); // Shouldn't violate any Constrains
    }

    @Test 
    public void testStoredData() throws InterruptedException {
        final PriceSample generatedItem = generatePriceSample(priceWatchId(24788));
        objectUnderTest.insert(generatedItem);
        final PriceSample foundItem = objectUnderTest.getAllByPriceWatchId(priceWatchId(24788)).get(0);
        comparePriceSamples(generatedItem, foundItem);
        final PriceSample foundItemById = objectUnderTest.getById(foundItem.getId());
        comparePriceSamples(generatedItem, foundItemById);
    }

    @Test 
    public void testMassInsert() throws InterruptedException {
        final PriceWatchId priceWatchId = priceWatchId(1233);
        final int insertCount = 400;
        final int baseSize = objectUnderTest.getAllByPriceWatchId(priceWatchId).size();

        for(int i=0 ; i<insertCount ; i++) {
            final PriceSample generatedItem = generatePriceSample(priceWatchId, i);
            objectUnderTest.insert(generatedItem);
            checkIfItemIsStored(priceWatchId, generatedItem);
        }

        assertEquals(baseSize + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId).size());
    }

    @Test 
    public void testListFetch() throws InterruptedException {
        final PriceWatchId priceWatchId = priceWatchId(1233);
        final int insertCount = 4;
        final int baseSize = objectUnderTest.getAllByPriceWatchId(priceWatchId).size();

        for(int i=0 ; i<insertCount ; i++) {
            final PriceSample generatedItem = generatePriceSample(priceWatchId, i);
            objectUnderTest.insert(generatedItem);
            checkIfItemIsStored(priceWatchId, generatedItem);
        }

        assertEquals(baseSize + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId).size());
    }


    @Test 
    public void testDeleteById() throws InterruptedException {
        final PriceWatchId priceWatchId = priceWatchId(5);
        final int insertCount = 40;
        final int baseSize = objectUnderTest.getAllByPriceWatchId(priceWatchId).size();

        for(int i=0 ; i<insertCount ; i++) {
            final PriceSample generatedItem = generatePriceSample(priceWatchId, i);
            objectUnderTest.insert(generatedItem);
        }
        assertEquals(baseSize + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId).size());

        final PriceSample foundItem = objectUnderTest.getAllByPriceWatchId(priceWatchId).get(10);
        objectUnderTest.delete(foundItem);
        checkIfItemIsNotStored(priceWatchId, foundItem);
    }

    @Test 
    public void testDeleteByMaxTimestamp() throws InterruptedException {
        final PriceWatchId priceWatchId1 = priceWatchId(50);
        final PriceWatchId priceWatchId2 = priceWatchId(51);
        final int insertCount = 100;
        final int baseSize1 = objectUnderTest.getAllByPriceWatchId(priceWatchId1).size();
        final int baseSize2 = objectUnderTest.getAllByPriceWatchId(priceWatchId2).size();

        for(int i=0 ; i<insertCount ; i++) {
            final PriceSample generatedItem1 = generatePriceSample(priceWatchId1, i);
            final PriceSample generatedItem2 = generatePriceSample(priceWatchId2, i+10);
            objectUnderTest.insert(generatedItem1);
            objectUnderTest.insert(generatedItem2);
        }
        assertEquals(baseSize1 + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId1).size());
        assertEquals(baseSize2 + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId2).size());

        objectUnderTest.deleteAllByMaxTimestamp(20);
        assertEquals(baseSize1 + insertCount - 20, objectUnderTest.getAllByPriceWatchId(priceWatchId1).size());
        assertEquals(baseSize2 + insertCount - 10, objectUnderTest.getAllByPriceWatchId(priceWatchId2).size());
        for(PriceSample sample : objectUnderTest.getAllByPriceWatchId(priceWatchId1)) {
            assertTrue(sample.getTimeInMilliseconds() >= 20);
        }        
        for(PriceSample sample : objectUnderTest.getAllByPriceWatchId(priceWatchId2)) {
            assertTrue(sample.getTimeInMilliseconds() >= 20);
        }
    }
    
    @Test 
    public void testAllForPriceWatchId() throws InterruptedException {
        final PriceWatchId priceWatchId1 = priceWatchId(150);
        final PriceWatchId priceWatchId2 = priceWatchId(151);
        final int insertCount = 100;
        final int baseSize1 = objectUnderTest.getAllByPriceWatchId(priceWatchId1).size();
        final int baseSize2 = objectUnderTest.getAllByPriceWatchId(priceWatchId2).size();

        for(int i=0 ; i<insertCount ; i++) {
            final PriceSample generatedItem1 = generatePriceSample(priceWatchId1, i);
            final PriceSample generatedItem2 = generatePriceSample(priceWatchId2, i+10);
            objectUnderTest.insert(generatedItem1);
            objectUnderTest.insert(generatedItem2);
        }
        assertEquals(baseSize1 + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId1).size());
        assertEquals(baseSize2 + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId2).size());

        objectUnderTest.deleteAllByPriceWatchId(priceWatchId1);
        assertEquals(0, objectUnderTest.getAllByPriceWatchId(priceWatchId1).size());
        assertEquals(baseSize2 + insertCount, objectUnderTest.getAllByPriceWatchId(priceWatchId2).size());
    }

    protected void checkIfItemIsStored(final PriceWatchId priceWatchId, final PriceSample requiredItem) {
        for(PriceSample item : objectUnderTest.getAllByPriceWatchId(priceWatchId)) {
            if(requiredItem.getId()==item.getId()) {
                comparePriceSamples(item, requiredItem);
                return;
            }
        }
        fail("Inserted Item (priceWatchId:" + priceWatchId.toInt() + ") not found!");
    }

    protected void checkIfItemIsNotStored(final PriceWatchId priceWatchId, final PriceSample requiredItem) {
        for(PriceSample item : objectUnderTest.getAllByPriceWatchId(priceWatchId)) {
            if(item.getId() == requiredItem.getId()) {
                fail("Found Item!");
            }
        }
    }
    protected void comparePriceSamples(final PriceSample expected, final PriceSample actual) {
        try {
            assertEquals(expected.getMedianPrice(), actual.getMedianPrice());
            assertEquals(expected.getAveragePrice(), actual.getAveragePrice());
            assertEquals(expected.getMinimumPrice(), actual.getMinimumPrice());
            assertEquals(expected.getPriceWatchId(), actual.getPriceWatchId());
            assertEquals(expected.getSampleSize(), actual.getSampleSize());
            assertEquals(expected.getTimeInMilliseconds(), actual.getTimeInMilliseconds());
        } catch (AssertionError e) {
            fail("Items differ!\n Expected:" + expected + "\n Actual: " + actual);
        }
    }
}
