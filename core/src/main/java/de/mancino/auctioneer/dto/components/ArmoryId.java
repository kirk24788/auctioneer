package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Armory Id.
 *
 * Entspricht der Id für Items aus der Blizzard
 * Armory und wird intern als int-Wert representiert.
 *
 * @author mmancino
 */
@XmlRootElement(name = "armoryid")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ArmoryId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int armoryId;

    public ArmoryId() {
        this.setArmoryId(1);
    }

    /**
     * Armory Id.
     *
     * Entspricht der Id für Items aus der Blizzard
     * Armory und wird intern als int-Wert representiert.
     *
     * @param armoryId Armory Id
     */
    public ArmoryId(final int armoryId) {
        if(armoryId < 1) {
            throw new IllegalArgumentException("Armory ID must be greater than 0! (was: " + armoryId);
        }
        this.setArmoryId(armoryId);
    }

    /**
     * Armory Id.
     *
     * Entspricht der Id für Items aus der Blizzard
     * Armory und wird intern als int-Wert representiert.
     *
     * @param armoryId Armory Id
     */
    public static ArmoryId armoryId(final int armoryId) {
        return new ArmoryId(armoryId);
    }

    /**
     * Gibt den int-Wert der Armory ID zurück.
     *
     * @return Armory ID
     */
    public int toInt() {
        return getArmoryId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getArmoryId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ArmoryId && ((ArmoryId)obj).getArmoryId() == getArmoryId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(getArmoryId());
    }

    /**
     * @return the armoryId
     */
    @XmlElement
    public int getArmoryId() {
        return armoryId;
    }

    /**
     * @param armoryId the armoryId to set
     */
    public void setArmoryId(int armoryId) {
        this.armoryId = armoryId;
    }
}
