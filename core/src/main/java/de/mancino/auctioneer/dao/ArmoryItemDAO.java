package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;

public interface ArmoryItemDAO extends Serializable {

    public ArmoryItem insert(final ArmoryItem armoryItem);

    public void update(final ArmoryItem armoryItem);

    public void delete(final ArmoryItem armoryItem);

    public ArmoryItem getByArmoryId(final ArmoryId armoryId);

    public ArmoryItem getByItemName(final ItemName itemName);
    
    public List<ArmoryItem> getAll();
    
    public int getSize();
}
