package de.mancino.auctioneer.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name="AUTO_BUYOUT")
public class AutoBuyout implements Serializable {
    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }
    /**
     * @param itemName the itemName to set
     */
    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }
    /**
     * @return the maxPrice
     */
    public long getMaxPrice() {
        return maxPrice;
    }
    /**
     * @param maxPrice the maxPrice to set
     */
    public void setMaxPrice(final long maxPrice) {
        this.maxPrice = maxPrice;
    }
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUYOUT_ID", unique = true, nullable = false)
    private int id;

    @Column(name = "ITEM_NAME", unique = true, nullable = false)
    private String itemName;
    @Column(name = "MAX_PRICE", unique = false, nullable = false)
    private long maxPrice;

    /** * @return Returns the id. */
    @XmlTransient
    public int getId() {
        return id;
    }
    /** * @param id The id to set. */
    public void setId(final int id) {
        this.id = id;
    }
}

