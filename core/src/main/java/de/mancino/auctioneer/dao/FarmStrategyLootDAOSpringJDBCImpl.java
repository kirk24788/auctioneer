package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.FarmStrategyId.farmStrategyId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.FarmStrategyId;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class FarmStrategyLootDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements FarmStrategyLootDAO {
    private static final long serialVersionUID = 1L;

    class FarmStrategyLootProxy extends FarmStrategyLoot {
        private final ArmoryItemDAO armoryItemDAO;
        private final ArmoryId armoryId;

        public FarmStrategyLootProxy(final FarmStrategyId farmStrategyId, final int itemCount,
                final JdbcTemplate jdbcTemplate, final ArmoryId armoryId) {
            super(farmStrategyId, itemCount, null);
            this.armoryItemDAO = new ArmoryItemDAOSpringJDBCImpl(jdbcTemplate);
            this.armoryId = armoryId;
        }

        @Override
        public synchronized ArmoryItem getItem() {
            if (super.getItem()==null) {
                super.setItem(armoryItemDAO.getByArmoryId(armoryId));
            }
            return super.getItem();
        }
    }

    private final SerializableParameterizedRowMapper<FarmStrategyLoot> farmStrategyLootMapper = new SerializableParameterizedRowMapper<FarmStrategyLoot>() {
        private static final long serialVersionUID = 1L;

        @Override
        public FarmStrategyLoot mapRow(ResultSet rs, int rowNum) throws SQLException {
            FarmStrategyLoot farmStrategyLoot = new FarmStrategyLootProxy(
                    farmStrategyId(rs.getInt("farmStrategyId")),
                    rs.getInt("itemCount"),
                    getJdbcTemplate(),
                    armoryId(rs.getInt("item")) );
            farmStrategyLoot.setId(rs.getLong("id"));
            return farmStrategyLoot;
        }
    };

    public FarmStrategyLootDAOSpringJDBCImpl(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public FarmStrategyLootDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("farmStrategyLoot")
                .dataTypes(bigInteger("id").autoIncrement(),
                        integer("farmStrategyId").notNull(),
                        integer("itemCount").notNull(),
                        integer("item").notNull())
                        .primaryKeys("id"));
    }

    @Override
    public FarmStrategyLoot insert(final FarmStrategyLoot farmStrategyLoot) {
        final String INSERT_STATEMENT = "INSERT INTO " +
                "farmStrategyLoot (farmStrategyId, itemCount, item) " +
                "VALUES (?,?,?)";
        FarmStrategyDAOSpringJDBCImpl.FarmStrategyProxy.invalidateFarmStrategyLoot(farmStrategyLoot.getFarmStrategyId());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, farmStrategyLoot.getFarmStrategyId().toInt());
                    ps.setLong(2, farmStrategyLoot.getItemCount());
                    ps.setLong(3, farmStrategyLoot.getItem().getArmoryId().toInt());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            long id = keyHolder.getKey().longValue();
            farmStrategyLoot.setId(id);
            return farmStrategyLoot;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    farmStrategyLoot.getFarmStrategyId().toInt(),
                    farmStrategyLoot.getItemCount(),
                    farmStrategyLoot.getItem().getArmoryId().toInt());
            long id = getSimpleJdbcTemplate().queryForLong("CALL IDENTITY();");
            farmStrategyLoot.setId(id);
            return farmStrategyLoot;
        }
    }

    @Override
    public void delete(FarmStrategyLoot farmStrategyLoot) {
        FarmStrategyDAOSpringJDBCImpl.FarmStrategyProxy.invalidateFarmStrategyLoot(farmStrategyLoot.getFarmStrategyId());
        getSimpleJdbcTemplate().update("DELETE FROM farmStrategyLoot WHERE id=?", farmStrategyLoot.getId());
    }

    @Override
    public void deleteAllByFarmStrategyId(FarmStrategyId farmStrategyId) {
        FarmStrategyDAOSpringJDBCImpl.FarmStrategyProxy.invalidateFarmStrategyLoot(farmStrategyId);
        getSimpleJdbcTemplate().update("DELETE FROM farmStrategyLoot WHERE farmStrategyId=?", farmStrategyId.toInt());

    }
    @Override
    public FarmStrategyLoot getById(long id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, farmStrategyId, itemCount, item " +
                "FROM farmStrategyLoot WHERE id=?", farmStrategyLootMapper, id);
    }

    @Override
    public List<FarmStrategyLoot> getAllByFarmStrategyId(FarmStrategyId farmStrategyId) {
        final List<FarmStrategyLoot> loot = getSimpleJdbcTemplate().query(
                "SELECT id, farmStrategyId, itemCount, item " +
                "FROM farmStrategyLoot WHERE farmStrategyId=?", farmStrategyLootMapper, farmStrategyId.toInt());
        if (loot==null) {
            return new ArrayList<FarmStrategyLoot>();
        } else {
            return loot;
        }
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM farmStrategyLoot");
    }
}
