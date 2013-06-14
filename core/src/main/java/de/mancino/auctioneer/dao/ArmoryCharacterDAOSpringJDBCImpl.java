/**
 *
 */
package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bool;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.DataType.varChar;
import static de.mancino.auctioneer.ddl.Table.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import de.mancino.armory.json.vault.AuctionFaction;
import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.dto.CashSample;
import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.CharacterName;
import de.mancino.auctioneer.dto.components.Color;
import de.mancino.auctioneer.dto.components.RealmName;
import de.mancino.utils.SerializableParameterizedRowMapper;

/**
 *
 * @author mmancino
 */
public class ArmoryCharacterDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements ArmoryCharacterDAO {
    private static final long serialVersionUID = 1L;

    static class ArmoryCharacterProxy extends ArmoryCharacter {
        private static final long serialVersionUID = 200L;

        private final CashSampleDAO cashSampleDAO;
        private final CharacterId characterId;
        private long nextAutomaticInvalidation = 0L;
        private static long CACHE_TIME_IN_MS = 2L * 60L * 1000L;
        private static final ConcurrentMap<CharacterId, List<CashSample>> cashSampleMap = new ConcurrentHashMap<CharacterId, List<CashSample>>();

        ArmoryCharacterProxy(final JdbcTemplate jdbcTemplate, final CharacterId characterId) {
            this.cashSampleDAO = new CashSampleDAOSpringJDBCImpl(jdbcTemplate);
            this.characterId = characterId;
            super.setCashSamples(null);
        }
        @Override
        public List<CashSample> getCashSamples() {
            automaticInvalidation();
            List<CashSample> cashSamples = cashSampleMap.get(characterId);
            if(cashSamples==null) {
                cashSamples = cashSampleDAO.getAllByCharacterId(characterId);
                cashSampleMap.put(characterId, cashSamples);
            }
            return cashSamples;
        }

        static void invalidateCashSamples(final CharacterId id) {
            cashSampleMap.remove(id);
        }

        static void invalidateAllCashSamples() {
            cashSampleMap.clear();
        }

        private void automaticInvalidation() {
            if(nextAutomaticInvalidation < System.currentTimeMillis() ) {
                invalidateAllCashSamples();
                nextAutomaticInvalidation = System.currentTimeMillis() + CACHE_TIME_IN_MS;
            }
        }
    }


    private final SerializableParameterizedRowMapper<ArmoryCharacter> armoryCharacterMapper = new SerializableParameterizedRowMapper<ArmoryCharacter>() {
        private static final long serialVersionUID = 1L;

        @Override
        public ArmoryCharacter mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArmoryCharacter armoryCharacter = new ArmoryCharacterProxy(getJdbcTemplate(), new CharacterId(rs.getInt("id")));
            armoryCharacter.setId(new CharacterId(rs.getInt("id")));
            armoryCharacter.setCharacterName(new CharacterName(rs.getString("characterName")));
            armoryCharacter.setFaction(AuctionFaction.valueOf(rs.getString("faction")));
            armoryCharacter.setRealmName(new RealmName(rs.getString("realmName")));
            armoryCharacter.setColor(new Color(rs.getString("color")));
            armoryCharacter.setBot(rs.getBoolean("bot"));
            armoryCharacter.setAverageItemLevel(rs.getInt("ilvl"));
            armoryCharacter.setAverageItemLevelEquipped(rs.getInt("ilvle"));
            armoryCharacter.setLevel(rs.getInt("level"));
            return armoryCharacter;
        }
    };

    public ArmoryCharacterDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public ArmoryCharacterDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("armoryCharacter")
                    .dataTypes(integer("id").autoIncrement(),
                               varChar("characterName").size(50).notNull(),
                               varChar("faction").size(10).notNull(),
                               varChar("realmName").size(50).notNull(),
                               varChar("color").size(6).notNull(),
                               integer("ilvl"),
                               integer("ilvle"),
                               integer("level"),
                               bool("bot").notNull())
                    .primaryKeys("id"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArmoryCharacter insert(ArmoryCharacter armoryCharacter) {
        getSimpleJdbcTemplate().update("INSERT INTO armoryCharacter (characterName, faction, realmName, color, ilvl, ilvle, level, bot) VALUES (?,?,?,?,?,?,?)",
                armoryCharacter.getCharacterName().toString(),
                armoryCharacter.getFaction().name(),
                armoryCharacter.getRealmName().toString(),
                armoryCharacter.getColor().toString(),
                armoryCharacter.getAverageItemLevel(),
                armoryCharacter.getAverageItemLevelEquipped(),
                armoryCharacter.getLevel(),
                armoryCharacter.isBot());
        return armoryCharacter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArmoryCharacter update(ArmoryCharacter armoryCharacter) {
        getSimpleJdbcTemplate().update("UPDATE armoryCharacter SET characterName=?, faction=?, realmName=?, color=?, ilvl=?, ilvle=?, level=?, bot=? WHERE id=?",
                armoryCharacter.getCharacterName().toString(),
                armoryCharacter.getFaction().name(),
                armoryCharacter.getRealmName().toString(),
                armoryCharacter.getColor().toString(),
                armoryCharacter.getAverageItemLevel(),
                armoryCharacter.getAverageItemLevelEquipped(),
                armoryCharacter.getLevel(),
                armoryCharacter.isBot(),
                armoryCharacter.getId().toInt());
        return getByCharacterId(armoryCharacter.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(ArmoryCharacter armoryCharacter) {
        getSimpleJdbcTemplate().update("DELETE FROM armoryCharacter WHERE id=?",
                armoryCharacter.getId().toInt());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArmoryCharacter getByCharacterId(CharacterId characterId) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, characterName, faction, realmName, color, ilvl, ilvle, level, bot " +
                "FROM armoryCharacter WHERE id=?",
                armoryCharacterMapper, characterId.toInt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArmoryCharacter getByCharacterNameAndRealm(final CharacterName characterName, final RealmName realmName) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, characterName, faction, realmName, color, ilvl, ilvle, level, bot " +
                "FROM armoryCharacter WHERE characterName=? AND realmName=?",
                armoryCharacterMapper, characterName.toString(), realmName.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ArmoryCharacter> getAll() {
        return getSimpleJdbcTemplate().query("SELECT id, characterName, faction, realmName, color, ilvl, ilvle, level, bot " +
                "FROM armoryCharacter ORDER BY characterName", armoryCharacterMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM armoryCharacter");
    }

}
