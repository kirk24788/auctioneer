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
public class CharacterName implements Comparable<CharacterName>,Serializable {
    private static final long serialVersionUID = 1L;

    private static final String NO_VALUE = "";

    @Column(unique = false, nullable = false)
    private String characterName;

    /**
     * @Deprecated Package-Protected Konstruktor - nur für Hibernate Injection!
     */
    @Deprecated
    CharacterName() {
        characterName = NO_VALUE;
    }

    /**
     * Charakter Name.
     * 
     * @param characterName Charakter Name als String
     */
    public CharacterName(final String characterName) {
        if(characterName == null || characterName.trim().isEmpty()) {
            throw new IllegalArgumentException("Character Name may not be null/empty!");
        }
        this.characterName = characterName.trim();
    }
    
    /**
     * Charakter Name.
     * 
     * @param characterName Charakter Name als String
     */
    public static CharacterName characterName(final String characterName) {
        return new CharacterName(characterName);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return characterName;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return characterName.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CharacterName && characterName.equals(((CharacterName)obj).characterName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(CharacterName o) {
        return characterName.compareTo(o.characterName);
    }
}
