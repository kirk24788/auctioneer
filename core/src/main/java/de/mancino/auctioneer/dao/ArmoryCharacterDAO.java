package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.RealmName;

public interface ArmoryCharacterDAO extends Serializable {

    public ArmoryCharacter insert(final ArmoryCharacter armoryItem);

    public ArmoryCharacter update(final ArmoryCharacter armoryItem);

    public void delete(final ArmoryCharacter armoryItem);

    public ArmoryCharacter getByCharacterId(final CharacterId characterId);

    public ArmoryCharacter getByCharacterNameAndRealm(final CharacterName characterName, final RealmName realmName);

    public List<ArmoryCharacter> getAll();

    public int getSize();
}
