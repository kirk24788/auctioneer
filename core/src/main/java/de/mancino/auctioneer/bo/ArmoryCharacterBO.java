package de.mancino.auctioneer.bo;

import java.io.Serializable;
import java.util.List;

import de.mancino.armory.json.vault.AuctionFaction;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.RealmName;
import de.mancino.auctioneer.exceptions.ArmoryCharacterAlreadyExistingException;
import de.mancino.auctioneer.exceptions.ArmoryCharacterDoesnNotExistException;

public interface ArmoryCharacterBO extends Serializable {
    public ArmoryCharacter createArmoryCharacter(final CharacterName characterName, AuctionFaction faction, final RealmName realmName, Color color) throws ArmoryCharacterAlreadyExistingException;
    public void addCashSample(final CharacterId characterId, final Currency cash, final long timestamp);
    public ArmoryCharacter findByNameAndRealm(final CharacterName characterName, final RealmName realmName) throws ArmoryCharacterDoesnNotExistException;
    public List<ArmoryCharacter> listArmoryCharacters();
    public List<ArmoryCharacter> listArmoryCharactersByName();
    public List<ArmoryCharacter> listArmoryCharactersByLevel();
    public List<ArmoryCharacter> listArmoryCharactersByItemLevel();
    public void deleteOldCashSamples(final long maxTimestamp);
    public ArmoryCharacter updateArmoryCharacter(final ArmoryCharacter armoryCharacter);
}