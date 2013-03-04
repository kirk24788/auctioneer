package de.mancino.auctioneer.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.mancino.armory.json.api.item.Item;
import de.mancino.auctioneer.dto.components.ArmoryIcon;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;

@XmlRootElement(name = "armoryitem")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ArmoryItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2L;
    private ArmoryId armoryId;
    private ItemName itemName;
    private ArmoryIcon armoryIcon;

    public ArmoryItem() {
    }

    public ArmoryItem(final Item item) {
        this.armoryId = new ArmoryId(item.id);
        this.itemName = new ItemName(item.name);
        this.armoryIcon = new ArmoryIcon(item);
    }

    public ArmoryItem(final ArmoryId armoryId, final ItemName itemName, final ArmoryIcon armoryIcon) {
        this.armoryId = armoryId;
        this.itemName = itemName;
        this.armoryIcon = armoryIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return itemName + " (" + armoryId.toInt() + ")" ;
    }


    /**
     * @return the armoryId
     */
    @XmlElement
    public ArmoryId getArmoryId() {
        return armoryId;
    }

    /**
     * @param armoryId the armoryId to set
     */
    public void setArmoryId(ArmoryId armoryId) {
        this.armoryId = armoryId;
    }

    /**
     * @return the itemName
     */
    @XmlElement
    public ItemName getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(ItemName itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the armoryIcon
     */
    @XmlElement
    public ArmoryIcon getArmoryIcon() {
        return armoryIcon;
    }

    /**
     * @param armoryIcon the armoryIcon to set
     */
    public void setArmoryIcon(ArmoryIcon armoryIcon) {
        this.armoryIcon = armoryIcon;
    }
}
