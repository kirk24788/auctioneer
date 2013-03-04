package de.mancino.auctioneer.dto.components;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;


/**
 * Character Name.
 * 
 * @author mmancino
 */
@Embeddable
@Entity
public class RealmName implements Comparable<RealmName>,Serializable {
    private static final long serialVersionUID = 1L;

    private static final String NO_VALUE = "";

    @Column(unique = false, nullable = false)
    private String realmName;

    /**
     * @Deprecated Package-Protected Konstruktor - nur für Hibernate Injection!
     */
    @Deprecated
    RealmName() {
        realmName = NO_VALUE;
    }

    /**
     * Realm Name.
     * 
     * @param characterName Charakter Name als String
     */
    public RealmName(final String realmName) {
        if(realmName == null || realmName.trim().isEmpty()) {
            throw new IllegalArgumentException("Realm Name may not be null/empty!");
        }
        this.realmName = realmName.trim();
    }
    
    /**
     * Realm Name.
     * 
     * @param characterName Realm Name als String
     */
    public static RealmName characterName(final String realmName) {
        return new RealmName(realmName);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return realmName;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return realmName.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof RealmName && realmName.equals(((RealmName)obj).realmName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(RealmName o) {
        return realmName.compareTo(o.realmName);
    }
}
