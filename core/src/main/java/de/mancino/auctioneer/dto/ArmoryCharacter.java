package de.mancino.auctioneer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.mancino.armory.json.vault.AuctionFaction;
import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.RealmName;

public class ArmoryCharacter implements Serializable, Comparable<ArmoryCharacter> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private CharacterId id;
    private CharacterName characterName;
    private AuctionFaction faction;
    private RealmName realmName;
    private List<CashSample> cashSamples;
    private Color color;
    private boolean bot;
    private int level;
    private int averageItemLevelEquipped;
    private int averageItemLevel;

    public ArmoryCharacter() {
        this(null, null, null, null);
    }

    public ArmoryCharacter(final CharacterName characterName, final AuctionFaction faction, final RealmName realmName, final Color color) {
        this.characterName = characterName;
        this.realmName = realmName;
        this.faction = faction;
        this.cashSamples = new ArrayList<CashSample>();
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if(characterName != null && realmName != null && id != null) {
            return characterName + "@" + realmName + " (" + id.toInt() + ") " + "Faction " + faction.key + " Level " + level + " iLvl " + averageItemLevelEquipped + "/" + averageItemLevel;
        } else {
            return "ArmoryCharacter<null>";
        }
    }

    public CharacterId getId() {
        return id;
    }

    public void setId(CharacterId id) {
        this.id = id;
    }

    public CharacterName getCharacterName() {
        return characterName;
    }

    public void setCharacterName(CharacterName characterName) {
        this.characterName = characterName;
    }

    public RealmName getRealmName() {
        return realmName;
    }

    public void setRealmName(RealmName realmName) {
        this.realmName = realmName;
    }

    public List<CashSample> getCashSamples() {
        return cashSamples;
    }

    public void setCashSamples(List<CashSample> cashSamples) {
        this.cashSamples = cashSamples;
    }

    public Currency getCurrentCash() {
        if(getCashSamples()==null || getCashSamples().size() == 0) {
            return new Currency(0L);
        }
        return getCashSamples().get(getCashSamples().size() -1).getCash();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(ArmoryCharacter o) {
        final String myName = characterName.toString() + "@" + realmName.toString();
        final String otherName = o.characterName.toString() + "@" + o.realmName.toString();
        return myName.compareTo(otherName);
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    /**
     * @return the averageItemLevel
     */
    public int getAverageItemLevel() {
        return averageItemLevel;
    }

    /**
     * @param averageItemLevel the averageItemLevel to set
     */
    public void setAverageItemLevel(int averageItemLevel) {
        this.averageItemLevel = averageItemLevel;
    }

    /**
     * @return the averageItemLevelEquipped
     */
    public int getAverageItemLevelEquipped() {
        return averageItemLevelEquipped;
    }

    /**
     * @param averageItemLevelEquipped the averageItemLevelEquipped to set
     */
    public void setAverageItemLevelEquipped(int averageItemLevelEquipped) {
        this.averageItemLevelEquipped = averageItemLevelEquipped;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public void setFaction(AuctionFaction faction) {
        this.faction = faction;
    }

    public AuctionFaction getFaction() {
        return faction;
    }
}
