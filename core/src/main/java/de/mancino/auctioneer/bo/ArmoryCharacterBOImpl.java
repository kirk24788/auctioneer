package de.mancino.auctioneer.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import de.mancino.armory.json.vault.AuctionFaction;
import de.mancino.auctioneer.dao.ArmoryCharacterDAO;
import de.mancino.auctioneer.dao.CashSampleDAO;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.dto.CashSample;
import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.RealmName;
import de.mancino.auctioneer.exceptions.ArmoryCharacterAlreadyExistingException;
import de.mancino.auctioneer.exceptions.ArmoryCharacterDoesnNotExistException;

public class ArmoryCharacterBOImpl implements ArmoryCharacterBO {
    private static final long serialVersionUID = 1L;

    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ArmoryCharacterBOImpl.class);

    private final ArmoryCharacterDAO armoryCharacterDAO;
    private final CashSampleDAO cashSampleDAO;

    protected ArmoryCharacterBOImpl() {
        this.armoryCharacterDAO = null;
        this.cashSampleDAO = null;
    }

    public ArmoryCharacterBOImpl(final ArmoryCharacterDAO armoryCharacterDAO, final CashSampleDAO cashSampleDAO) {
        this.armoryCharacterDAO = armoryCharacterDAO;
        this.cashSampleDAO = cashSampleDAO;
    }

    @Override
    public ArmoryCharacter createArmoryCharacter(CharacterName characterName, AuctionFaction faction, RealmName realmName, Color color)
            throws ArmoryCharacterAlreadyExistingException {
        try {
            LOG.info("Creating Armory Character for: {}@{}", characterName, realmName);
            final ArmoryCharacter armoryCharacter = new ArmoryCharacter(characterName, faction, realmName, color);
            armoryCharacterDAO.insert(armoryCharacter);
            return armoryCharacter;
        } catch (final DataIntegrityViolationException e) {
            LOG.error("Error creating Armory Item", e);
            throw new ArmoryCharacterAlreadyExistingException(characterName, realmName);
        }
    }

    @Override
    public void addCashSample(CharacterId characterId, Currency cash, long timestamp) {
        cashSampleDAO.insert(new CashSample(characterId, timestamp, cash));
    }

    @Override
    public ArmoryCharacter findByNameAndRealm(CharacterName characterName, RealmName realmName)
            throws ArmoryCharacterDoesnNotExistException {
        try {
            LOG.debug("Searching Armory Character '{}@{}'", characterName, realmName);
            return armoryCharacterDAO.getByCharacterNameAndRealm(characterName, realmName);
        } catch (final EmptyResultDataAccessException e) {
            LOG.warn("Armory Character {}@{} not found!", characterName, realmName);
            throw new ArmoryCharacterDoesnNotExistException(characterName, realmName);
        }
    }

    @Override
    public List<ArmoryCharacter> listArmoryCharacters() {
        return armoryCharacterDAO.getAll();
    }

    @Override
    public void deleteOldCashSamples(long maxTimestamp) {
        cashSampleDAO.deleteAllByMaxTimestamp(maxTimestamp);
    }

    @Override
    public ArmoryCharacter updateArmoryCharacter(ArmoryCharacter armoryCharacter) {
        return armoryCharacterDAO.update(armoryCharacter);
    }

    @Override
    public List<ArmoryCharacter> listArmoryCharactersByName() {
        List<ArmoryCharacter> sortList = new ArrayList<>(armoryCharacterDAO.getAll());
        Collections.sort(sortList, new Comparator<ArmoryCharacter>() {
            @Override
            public int compare(ArmoryCharacter o1, ArmoryCharacter o2) {
                String str1 = o1.getCharacterName().toString() + "-" + o1.getRealmName().toString();
                String str2 = o2.getCharacterName().toString() + "-" + o2.getRealmName().toString();
                return str1.compareTo(str2);
            }
        });
        return Collections.unmodifiableList(sortList);
    }

    @Override
    public List<ArmoryCharacter> listArmoryCharactersByLevel() {
        List<ArmoryCharacter> sortList = new ArrayList<>(armoryCharacterDAO.getAll());
        Collections.sort(sortList, new Comparator<ArmoryCharacter>() {
            @Override
            public int compare(ArmoryCharacter o1, ArmoryCharacter o2) {
                return o2.getLevel() - o1.getLevel();
            }
        });
        return Collections.unmodifiableList(sortList);
    }

    @Override
    public List<ArmoryCharacter> listArmoryCharactersByItemLevel() {
        List<ArmoryCharacter> sortList = new ArrayList<>(armoryCharacterDAO.getAll());
        Collections.sort(sortList, new Comparator<ArmoryCharacter>() {
            @Override
            public int compare(ArmoryCharacter o1, ArmoryCharacter o2) {
                return o2.getAverageItemLevel() - o1.getAverageItemLevel();
            }
        });
        return Collections.unmodifiableList(sortList);
    }

}
