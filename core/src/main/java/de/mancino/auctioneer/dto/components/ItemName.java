package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.mancino.armory.json.api.item.Item;


/**
 * Item Name.
 *
 * @author mmancino
 */
@Embeddable
@Entity@XmlRootElement(name = "itemname")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemName implements Comparable<ItemName>,Serializable {
    private static final long serialVersionUID = 1L;

    private static final String NO_VALUE = "";

    @Column(unique = false, nullable = false)
    private String itemName;

    /**
     * @Deprecated Package-Protected Konstruktor - nur für Hibernate Injection!
     */
    @Deprecated
    ItemName() {
        setItemName(NO_VALUE);
    }

    /**
     * Item Name.
     *
     * @param itemName Item Name als String
     */
    public ItemName(final String itemName) {
        if(itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item Name may not be null/empty!");
        }
        this.setItemName(itemName.trim());
    }

    /**
     * Item Name.
     *
     * @param itemTooltip Tooltip aus dem der Item-Name gezogen werden soll
     */
    public ItemName(final Item item) {
        this(item.name);
    }

    /**
     * Item Name.
     *
     * @param itemName Item Name als String
     */
    public static ItemName itemName(final String itemName) {
        return new ItemName(itemName);
    }
    /**
     * Item Name.
     *
     * @param itemTooltip Tooltip aus dem der Item-Name gezogen werden soll
     */
    public static ItemName itemName(Item item) {
        return new ItemName(item);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getItemName();
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getItemName().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemName && getItemName().equals(((ItemName)obj).getItemName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(ItemName o) {
        return getItemName().compareTo(o.getItemName());
    }

    /**
     * @return the itemName
     */
    @XmlElement
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
