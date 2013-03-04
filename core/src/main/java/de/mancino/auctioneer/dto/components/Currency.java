package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Währung.
 *
 * Die Währung wird repräsentiert in den den Einheiten Gold, Silber
 * und Kupfer.
 * Dabei ist Kupfer die kleinste Einheit, ein Silber entspricht 100
 * Kupfer und ein Gold entspricht 100 Silber bzw. 10000 Kupfer.
 *
 * @author mmancino
 */
@Embeddable
@Entity
@XmlRootElement(name = "currency")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Currency implements Comparable<Currency>, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(unique = false, nullable = false)
    private int gold;

    @Column(unique = false, nullable = false)
    private int silver;

    @Column(unique = false, nullable = false)
    private int copper;

    /**
     * @Deprecated Package-Protected Konstruktor - nur für Hibernate Injection!
     */
    @Deprecated
    Currency() {
        this(0,0,0);
    }

    /**
     * Währung.
     *
     * Die Währung wird repräsentiert in den den Einheiten Gold, Silber
     * und Kupfer.
     * Dabei ist Kupfer die kleinste Einheit, ein Silber entspricht 100
     * Kupfer und ein Gold entspricht 100 Silber bzw. 10000 Kupfer.
     *
     * @param price Preis in Kupfer
     */
    public Currency(final long price) {
        this((int)(price/10000L), (int)(price%10000L/100L) ,(int)(price%100L));
    }

    /**
     * Währung.
     *
     * Die Währung wird repräsentiert in den den Einheiten Gold, Silber
     * und Kupfer.
     * Dabei ist Kupfer die kleinste Einheit, ein Silber entspricht 100
     * Kupfer und ein Gold entspricht 100 Silber bzw. 10000 Kupfer.
     *
     * @param gold Gold-Anteil
     * @param silver Silber-Anteil - sollte zwischen 0 und 99 sein!
     * @param copper Kupfer-Anteil - sollte zwischen 0 und 99 sein!
     */
    public Currency(final int gold, final int silver, final int copper) {
        this.gold =  gold;
        this.silver = silver;
        this.copper = copper;
    }

    /**
     * Währung.
     *
     * Die Währung wird repräsentiert in den den Einheiten Gold, Silber
     * und Kupfer.
     * Dabei ist Kupfer die kleinste Einheit, ein Silber entspricht 100
     * Kupfer und ein Gold entspricht 100 Silber bzw. 10000 Kupfer.
     *
     * @param price Preis in Kupfer
     */
    public static Currency currency(final long price) {
        return new Currency(price);
    }

    /**
     * Währung.
     *
     * Die Währung wird repräsentiert in den den Einheiten Gold, Silber
     * und Kupfer.
     * Dabei ist Kupfer die kleinste Einheit, ein Silber entspricht 100
     * Kupfer und ein Gold entspricht 100 Silber bzw. 10000 Kupfer.
     *
     * @param gold Gold-Anteil
     * @param silver Silber-Anteil - sollte zwischen 0 und 99 sein!
     * @param copper Kupfer-Anteil - sollte zwischen 0 und 99 sein!
     */
    public static Currency currency(final int gold, final int silver, final int copper) {
        return new Currency(gold, silver, copper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if(toLong()>=0) {
            return gold + "g" + silver + "s" + copper + "c";
        } else {
            return "-" + (-1*gold) + "g" + (-1*silver) + "s" + (-1*copper) + "c";
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (int) toLong();
    }

    /**
     * Gibt die Währung als long zurück.
     *
     * @return Währung als long
     */
    public long toLong() {
        return getGold() * 10000L + silver * 100L + copper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Currency o) {
        return (int) (toLong()-o.toLong());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Currency) {
            Currency o = (Currency) obj;
            return o.gold==gold && o.silver==silver && o.copper==copper;
        }
        return false;
    }

    /**
     * Gold-Anteil
     *
     * @return Gold-Anteil
     */
    @XmlElement
    public int getGold() {
        return gold;
    }

    /**
     * Silber-Anteil - sollte zwischen 0 und 99 sein!
     *
     * @return Silber-Anteil - sollte zwischen 0 und 99 sein!
     */
    @XmlElement
    public int getSilver() {
        return silver;
    }

    /**
     * Kupfer-Anteil - sollte zwischen 0 und 99 sein!
     *
     * @return Kupfer-Anteil - sollte zwischen 0 und 99 sein!
     */
    @XmlElement
    public int getCopper() {
        return copper;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public void setCopper(int copper) {
        this.copper = copper;
    }


}