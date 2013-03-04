package de.mancino.auctioneer.aspell;

import java.util.ArrayList;
import java.util.List;

import de.mancino.armory.json.api.item.Item;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryIcon;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;
import de.mancino.auctioneer.exceptions.WowHeadItemAlreadyExistingException;

public class ASpellTestArmoryItemBO implements ArmoryItemBO {
    private static final long serialVersionUID = 1L;

    private List<ArmoryItem> armoryItems = new ArrayList<>();

    public void clearItemDB() {
        armoryItems.clear();
    }

    public ArmoryItem addItem(final int id, final String name) {
        ArmoryItem ai = new ArmoryItem(new ArmoryId(id), new ItemName(name), new ArmoryIcon(""));
        armoryItems.add(ai);
        return ai;
    }

    @Override
    public ArmoryItem createArmoryItem(Item item) throws ArmoryItemAlreadyExistingException {
        return addItem(item.id, item.name);
    }

    @Override
    public ArmoryItem createWowHeadItem(de.mancino.armory.xml.wowhead.item.Item item) throws WowHeadItemAlreadyExistingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArmoryItem findByItemName(ItemName itemName) throws ArmoryItemDoesNotExistException {
        for(ArmoryItem aI : armoryItems) {
            if(aI.getItemName().toString().equals(itemName.toString())) {
                return aI;
            }
        }
        throw new ArmoryItemDoesNotExistException(itemName);
    }

    @Override
    public ArmoryItem findByArmoryId(ArmoryId armoryId) throws ArmoryItemDoesNotExistException {
        for(ArmoryItem aI : armoryItems) {
            if(aI.getArmoryId().toInt() == armoryId.toInt()) {
                return aI;
            }
        }
        throw new ArmoryItemDoesNotExistException(armoryId);
    }

    @Override
    public List<ArmoryItem> listArmoryItems() {
        // TODO Auto-generated method stub
        return null;
    }

}
