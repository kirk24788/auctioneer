package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;
import de.mancino.auctioneer.exceptions.WowHeadItemAlreadyExistingException;

public interface ArmoryItemBO extends Serializable {
    public ArmoryItem createArmoryItem(final de.mancino.armory.json.api.item.Item item) throws ArmoryItemAlreadyExistingException;
    public ArmoryItem createWowHeadItem(final de.mancino.armory.xml.wowhead.item.Item item) throws WowHeadItemAlreadyExistingException;


    public ArmoryItem findByItemName(final ItemName itemName) throws ArmoryItemDoesNotExistException;

    public ArmoryItem findByArmoryId(final ArmoryId armoryId) throws ArmoryItemDoesNotExistException;

    public List<ArmoryItem> listArmoryItems();
}