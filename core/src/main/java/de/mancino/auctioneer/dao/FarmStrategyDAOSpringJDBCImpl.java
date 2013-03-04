package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.DataType.varChar;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.FarmStrategyProfit;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.FarmStrategyId;
import de.mancino.utils.DAOCacheMap;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class FarmStrategyDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements FarmStrategyDAO {
    private static final long serialVersionUID = 1L;

    static class FarmStrategyProxy extends FarmStrategy {
        private static final DAOCacheMap<FarmStrategyId, List<FarmStrategyLoot>,FarmStrategyLootDAO> lootMap =
                new DAOCacheMap<FarmStrategyId, List<FarmStrategyLoot>,FarmStrategyLootDAO>() {
            @Override
            protected List<FarmStrategyLoot> reload(FarmStrategyId key, FarmStrategyLootDAO dataStore) {
                return dataStore.getAllByFarmStrategyId(key);
            }
        };
        private static final DAOCacheMap<FarmStrategyId, List<FarmStrategyProfit>,FarmStrategyProfitDAO> profitMap =
                new DAOCacheMap<FarmStrategyId, List<FarmStrategyProfit>,FarmStrategyProfitDAO>() {
            @Override
            protected List<FarmStrategyProfit> reload(FarmStrategyId key, FarmStrategyProfitDAO dataStore) {
                return dataStore.getAllByFarmStrategyId(key);
            }
        };

        private static final long serialVersionUID = 100L;

        private final ArmoryItemDAO armoryItemDAO;
        private final ArmoryId iconItemId;

        FarmStrategyProxy(final JdbcTemplate jdbcTemplate, final String strategyName, final Currency additionalProfits,
                final ArmoryId iconItemId) {
            super(strategyName, null, additionalProfits);
            super.setProfits(null);
            super.setLoot(null);
            this.armoryItemDAO = new ArmoryItemDAOSpringJDBCImpl(jdbcTemplate);
            lootMap.setDataStore(new FarmStrategyLootDAOSpringJDBCImpl(jdbcTemplate));
            profitMap.setDataStore(new FarmStrategyProfitDAOSpringJDBCImpl(jdbcTemplate));
            this.iconItemId = iconItemId;
        }

        @Override
        public List<FarmStrategyLoot> getLoot() {
            return lootMap.get(super.getId());
        }

        @Override
        public List<FarmStrategyProfit> getProfits() {
            return profitMap.get(super.getId());
        }

        @Override
        public ArmoryItem getIconItem() {
            if (super.getIconItem()==null) {
                super.setIconItem(armoryItemDAO.getByArmoryId(iconItemId));
            }
            return super.getIconItem();
        }

        static void invalidateFarmStrategyLoot(final FarmStrategyId id) {
            lootMap.invalidate(id);
        }

        static void invalidateAllFarmStrategyLoot() {
            lootMap.invalidateAll();
        }

        static void invalidateFarmStrategyProfits(final FarmStrategyId id) {
            profitMap.invalidate(id);
        }

        static void invalidateAllFarmStrategyProfits() {
            profitMap.invalidateAll();
        }
    }


    private final SerializableParameterizedRowMapper<FarmStrategy> farmStrategyMapper = new SerializableParameterizedRowMapper<FarmStrategy>() {
        private static final long serialVersionUID = 1L;

        @Override
        public FarmStrategy mapRow(ResultSet rs, int rowNum) throws SQLException {
            FarmStrategy farmStrategy = new FarmStrategyProxy(getJdbcTemplate(),
                    rs.getString("name"),
                    Currency.currency(rs.getLong("additionalProfits")),
                    armoryId(rs.getInt("iconItemId"))
                    );
            farmStrategy.setId(FarmStrategyId.farmStrategyId(rs.getInt("id")));
            return farmStrategy;
        }
    };

    public FarmStrategyDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public FarmStrategyDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("farmStrategy")
                .dataTypes(
                        integer("id").autoIncrement(),
                        integer("iconItemId").notNull(),
                        varChar("name").notNull(),
                        bigInteger("additionalProfits").notNull())
                        .primaryKeys("id"));
    }

    @Override
    public FarmStrategy insert(final FarmStrategy farmStrategy) {
        final String INSERT_STATEMENT = "INSERT INTO farmStrategy "
                + "(iconItemId, name, additionalProfits) "
                + "VALUES (?, ?, ?)";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, farmStrategy.getIconItem().getArmoryId().toInt());
                    ps.setString(2, farmStrategy.getStrategyName());
                    ps.setLong(3, farmStrategy.getAdditionalProfits().toLong());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            int id = keyHolder.getKey().intValue();
            farmStrategy.setId(new FarmStrategyId(id));
            return farmStrategy;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    farmStrategy.getIconItem().getArmoryId().toInt(),
                    farmStrategy.getStrategyName(),
                    farmStrategy.getAdditionalProfits().toLong()
                    );
            int id = getSimpleJdbcTemplate().queryForInt("CALL IDENTITY();");
            farmStrategy.setId(new FarmStrategyId(id));
            return farmStrategy;
        }
    }

    @Override
    public void delete(final FarmStrategyId farmStrategyId) {
        getSimpleJdbcTemplate().update("DELETE FROM farmStrategy WHERE id=?",
                farmStrategyId.toInt());
    }

    @Override
    public List<FarmStrategy> getAll() {
        return getSimpleJdbcTemplate().query("SELECT id, iconItemId, name, additionalProfits "
                + "FROM farmStrategy", farmStrategyMapper);
    }

    @Override
    public FarmStrategy getById(FarmStrategyId id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, iconItemId, name, additionalProfits "
                + "FROM farmStrategy "
                + "WHERE id=?", farmStrategyMapper, id.toInt());
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM farmStrategy");
    }

    @Override
    public void update(FarmStrategy farmStrategy) {
        getSimpleJdbcTemplate().update("UPDATE farmStrategy "
                + "SET iconItemId=?, name=?, additionalProfits=? "
                + "WHERE id=?",
                farmStrategy.getIconItem().getArmoryId().toInt(),
                farmStrategy.getStrategyName(),
                farmStrategy.getAdditionalProfits().toLong());
    }

}
