package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.dto.components.ArmoryIcon.armoryIcon;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.ItemName.itemName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import de.mancino.armory.json.api.item.Item;
import de.mancino.auctioneer.ContextTest;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryIcon;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;

public class ArmoryItemDAOTest extends ContextTest {
    private static ArmoryItemDAO objectUnderTest;

    public static Item createJsonItem(final String itemName) {
        return createJsonItem(itemName(itemName));
    }

    public static Item createJsonItem(final ItemName itemName) {
        final Item itemTooltip = new Item();
        itemTooltip.id = (int) nextId();
        itemTooltip.name = itemName.toString();
        itemTooltip.icon = "inv_staff_13";
        return itemTooltip;
    }

    public static ArmoryItem createArmoryItem(final String itemName) {
        return createArmoryItem(itemName(itemName));
    }

    public static ArmoryItem createArmoryItem(final ItemName itemName) {
        return createArmoryItem(armoryIcon(nextByteArray()), armoryId((int) nextId()), itemName);
    }

    public static ArmoryItem createArmoryItem(final ArmoryIcon armoryIcon, final ArmoryId armoryId, final ItemName itemName) {
        ArmoryItem ai = new ArmoryItem();
        ai.setArmoryIcon(armoryIcon);
        ai.setArmoryId(armoryId);
        ai.setItemName(itemName);
        return ai;
    }

    @BeforeClass
    public static void initObjectUnderTest() throws ArmoryItemAlreadyExistingException {
        objectUnderTest = (ArmoryItemDAO) context.getBean("armoryItemDAO");
    }

    @Test
    public void testDoubleInsert() throws InterruptedException {
        final ArmoryItem item = createArmoryItem("Double Insert");
        objectUnderTest.insert(item);
        try {
            objectUnderTest.insert(item);
            fail("Second insert should throw DataIntegrityViolationException!");
        } catch (DataIntegrityViolationException e) {

        }
    }

    @Test
    public void testStoredDataByArmoryId() throws InterruptedException {
        final ArmoryItem generatedItem = createArmoryItem(itemName("New Insert"));
        objectUnderTest.insert(generatedItem);
        final ArmoryItem foundItem = objectUnderTest.getByArmoryId(generatedItem.getArmoryId());
        assertEquals(generatedItem.getArmoryId().toInt(), foundItem.getArmoryId().toInt());
        assertEquals(generatedItem.getItemName().toString(), foundItem.getItemName().toString());
        assertEquals(new String(generatedItem.getArmoryIcon().toByteArray()),
                new String(foundItem.getArmoryIcon().toByteArray()));
    }

    @Test
    public void testStoredDataByItemName() throws InterruptedException {
        final ItemName itemName = itemName("New Insert 2");
        final ArmoryItem generatedItem = createArmoryItem(itemName);
        objectUnderTest.insert(generatedItem);
        final ArmoryItem foundItem = objectUnderTest.getByItemName(itemName);
        assertEquals(generatedItem.getArmoryId().toInt(), foundItem.getArmoryId().toInt());
        assertEquals(generatedItem.getItemName().toString(), foundItem.getItemName().toString());
        assertEquals(new String(generatedItem.getArmoryIcon().toByteArray()),
                new String(foundItem.getArmoryIcon().toByteArray()));
    }

    @Test
    public void testListFetch() throws InterruptedException {
        final int insertCount = 4;
        final int baseSize = objectUnderTest.getAll().size();

        for(int i=1 ; i<=insertCount ; i++) {
            final ArmoryItem generatedItem = createArmoryItem(itemName("testListFetch Insert " + i));
            objectUnderTest.insert(generatedItem);
            checkIfItemIsStored(generatedItem);
        }

        assertEquals(baseSize + insertCount, objectUnderTest.getAll().size());
    }
    @Test
    public void testDelete() throws InterruptedException {
        final ArmoryItem generatedItem = createArmoryItem(itemName("testListFetch Delete"));
        objectUnderTest.insert(generatedItem);
        checkIfItemIsStored(generatedItem);
        objectUnderTest.delete(generatedItem);
        checkIfItemIsNotStored(generatedItem);
    }

    private void checkIfItemIsStored(final ArmoryItem requiredItem) {
        boolean found = false;
        for(ArmoryItem item : objectUnderTest.getAll()) {
            if(item.getArmoryId().equals(requiredItem.getArmoryId())) {

                assertEquals(requiredItem.getItemName().toString(), item.getItemName().toString());
                assertEquals(new String(requiredItem.getArmoryIcon().toByteArray()),
                        new String(item.getArmoryIcon().toByteArray()));
                found = true;
            }
        }
        assertTrue("Inserted Item not found!", found);
    }

    private void checkIfItemIsNotStored(final ArmoryItem requiredItem) {
        for(ArmoryItem item : objectUnderTest.getAll()) {
            if(item.getArmoryId().equals(requiredItem.getArmoryId())) {
                fail("Found Item!");
            }
        }
    }
}
