package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.BeanGenerator.generatePriceWatch;
import static de.mancino.auctioneer.dao.ArmoryItemDAOTest.createJsonItem;
import static de.mancino.auctioneer.dto.components.ItemName.itemName;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import de.mancino.auctioneer.BeanGenerator;
import de.mancino.auctioneer.ContextTest;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;

public class PriceWatchDAOTest extends ContextTest {

    private static PriceWatchDAO objectUnderTest;
    private static ArmoryItemBO armoryItemBO;

    private static final ItemName uniqueItemName = itemName("Magic Mushroom XY");
    private static ArmoryItem uniqueItem;

    private static final ItemName storeItemName = itemName("Magic Mushroom X2");
    private static ArmoryItem storeItem;

    private static final ItemName deleteItemName = itemName("Magic Mushroom DEL");
    private static ArmoryItem deleteItem;

    private static final ItemName deleteAll1ItemName = itemName("Magic Mushroom DEL-ALL1");
    private static ArmoryItem deleteAll1Item;
    private static final ItemName deleteAll2ItemName = itemName("Magic Mushroom DEL-ALL2");
    private static ArmoryItem deleteAll2Item;

    private static final ItemName cascadingDeleteItemName = itemName("Magic Mushroom CAS-DEL");
    private static ArmoryItem cascadingDeleteItem;

    private static final ItemName insertItemName = itemName("Magic Mushroom INS");
    private static ArmoryItem insertItem;


    @BeforeClass
    public static void initObjectUnderTest() throws ArmoryItemAlreadyExistingException {
        objectUnderTest = (PriceWatchDAO) context.getBean("priceWatchDAO");
        armoryItemBO = (ArmoryItemBO) context.getBean("armoryItemBO");
        uniqueItem = armoryItemBO.createArmoryItem(createJsonItem(uniqueItemName));
        storeItem = armoryItemBO.createArmoryItem(createJsonItem(storeItemName));
        deleteItem = armoryItemBO.createArmoryItem(createJsonItem(deleteItemName));
        deleteAll1Item = armoryItemBO.createArmoryItem(createJsonItem(deleteAll1ItemName));
        deleteAll2Item = armoryItemBO.createArmoryItem(createJsonItem(deleteAll2ItemName));
        cascadingDeleteItem = armoryItemBO.createArmoryItem(createJsonItem(cascadingDeleteItemName));
        insertItem = armoryItemBO.createArmoryItem(createJsonItem(insertItemName));
    }


    @Test
    public void testUniqueNameConstraint() {
        objectUnderTest.insert(generatePriceWatch(uniqueItem));
        try {
            objectUnderTest.insert(BeanGenerator.generatePriceWatch(uniqueItem));
            fail("DataIntegrityException expected after duplicate INSERT!");
        } catch (DataIntegrityViolationException e) {

        }
    }

    @Test
    public void testDataDeletion() {
        final PriceWatch generatedPriceWatch = generatePriceWatch(deleteItem);
        objectUnderTest.insert(generatedPriceWatch);
        final PriceWatch foundPriceWatch = objectUnderTest.getByArmoryId(deleteItem.getArmoryId());
        objectUnderTest.delete(foundPriceWatch);
        try {
            objectUnderTest.getByArmoryId(deleteItem.getArmoryId());
            fail("Object should have been deleted!");
        } catch (EmptyResultDataAccessException e) {
            // @Test(expected...) wird nicht verwendet, da findByName mehrfach vorkommt und nur hier abgefangen werden darf!
        }
    }

    @Test
    public void testHighLight() {
        final PriceWatch generatedPriceWatch = generatePriceWatch(storeItem);
        objectUnderTest.insert(generatedPriceWatch);
        final PriceWatch foundPriceWatch = objectUnderTest.getByArmoryId(storeItem.getArmoryId());
        Assert.assertFalse(foundPriceWatch.isHighlighted());

        foundPriceWatch.setHighlighted(true);
        objectUnderTest.update(foundPriceWatch);
        final PriceWatch foundAgainPriceWatch = objectUnderTest.getByArmoryId(storeItem.getArmoryId());
        Assert.assertTrue(foundAgainPriceWatch.isHighlighted());

    }

    @Test
    public void testInsertNewPriceSample() {
        objectUnderTest.getAll();
    }
}
