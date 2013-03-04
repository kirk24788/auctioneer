package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bool;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.PriceWatchId.priceWatchId;

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
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.PriceWatchId;
import de.mancino.utils.DAOCacheMap;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class PriceWatchDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements PriceWatchDAO {
    private static final long serialVersionUID = 1L;

    static class PriceWatchProxy extends PriceWatch {
        private static final long serialVersionUID = 200L;

        private final ArmoryItemDAO armoryItemDAO;
        private final ArmoryId armoryId;
        private static final DAOCacheMap<PriceWatchId, List<PriceSample>,PriceSampleDAO> priceSampleMap =
                new DAOCacheMap<PriceWatchId, List<PriceSample>,PriceSampleDAO>() {
            @Override
            protected List<PriceSample> reload(PriceWatchId key, PriceSampleDAO dataStore) {
                return dataStore.getAllByPriceWatchId(key);
            }
        };

        PriceWatchProxy(final JdbcTemplate jdbcTemplate, final ArmoryId armoryId) {
            priceSampleMap.setDataStore(new PriceSampleDAOSpringJDBCImpl(jdbcTemplate));
            this.armoryItemDAO = new ArmoryItemDAOSpringJDBCImpl(jdbcTemplate);
            this.armoryId = armoryId;
            super.setPriceSamples(null);
            super.setArmoryItem(null);
            super.setHighlighted(false);
        }

        @Override
        public ArmoryItem getArmoryItem() {
            if (super.getArmoryItem()==null) {
                super.setArmoryItem(armoryItemDAO.getByArmoryId(armoryId));
            }
            return super.getArmoryItem();
        }

        @Override
        public List<PriceSample> getPriceSamples() {
            return priceSampleMap.get(super.getId());
        }

        static void invalidatePriceSamples(final PriceWatchId id) {
            priceSampleMap.invalidate(id);
        }

        static void invalidateAllPriceSamples() {
            priceSampleMap.invalidateAll();
        }
    }


    /**
     * @param initDdl
     */
    public PriceWatchDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("priceWatch")
                .dataTypes(integer("id").autoIncrement(),
                        integer("armoryId").notNull(),
                        bool("highlighted").notNull())
                        .primaryKeys("id")
                        .uniqueKeys("armoryId"));
    }

    private final SerializableParameterizedRowMapper<PriceWatch> priceWatchMapper = new SerializableParameterizedRowMapper<PriceWatch>() {
        private static final long serialVersionUID = 1L;

        @Override
        public PriceWatch mapRow(ResultSet rs, int rowNum) throws SQLException {
            PriceWatch priceWatch = new PriceWatchProxy(getJdbcTemplate(), armoryId(rs.getInt("armoryId")));
            priceWatch.setId(priceWatchId(rs.getInt("id")));
            priceWatch.setHighlighted(rs.getBoolean("highlighted"));
            return priceWatch;
        }
    };

    @Override
    public PriceWatch insert(final PriceWatch priceWatch) {
        final String INSERT_STATEMENT = "INSERT INTO priceWatch (armoryId, highlighted) VALUES (?, ?)";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, priceWatch.getArmoryItem().getArmoryId().toInt());
                    ps.setBoolean(2, priceWatch.isHighlighted());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            int id = keyHolder.getKey().intValue();
            priceWatch.setId(new PriceWatchId(id));
            return priceWatch;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    priceWatch.getArmoryItem().getArmoryId().toInt(),
                    priceWatch.isHighlighted());
            int id = getSimpleJdbcTemplate().queryForInt("CALL IDENTITY();");
            priceWatch.setId(new PriceWatchId(id));
            return priceWatch;
        }
    }

    @Override
    public void delete(final PriceWatch priceSample) {
        getSimpleJdbcTemplate().update("DELETE FROM priceWatch WHERE id=?",
                priceSample.getId().toInt());
    }

    @Override
    public List<PriceWatch> getAll() {
        return getSimpleJdbcTemplate().query("SELECT w.id, w.armoryId, w.highlighted FROM priceWatch AS w, armoryItem AS a " +
                "WHERE w.armoryId=a.armoryId ORDER BY a.itemName;", priceWatchMapper);
    }

    @Override
    public PriceWatch getByArmoryId(final ArmoryId armoryId) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, armoryId, highlighted FROM priceWatch WHERE armoryId=?",
                priceWatchMapper, armoryId.toInt());
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM priceWatch");
    }

    @Override
    public int getSize(final boolean highlighted) {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM priceWatch WHERE highlighted=?", highlighted);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final PriceWatch priceWatch) {
        getSimpleJdbcTemplate().update("UPDATE priceWatch SET armoryId=?, highlighted=? WHERE id=?",
                priceWatch.getArmoryItem().getArmoryId().toInt(),
                priceWatch.isHighlighted(),
                priceWatch.getId().toInt());
    }

    /**
     * {@inheritDoc}
     * @return
     * @return
     */
    @Override
    public List<PriceWatch> getAllByHighlighted(final boolean highlighted) {
        return getSimpleJdbcTemplate().query("SELECT w.id, w.armoryId, w.highlighted FROM priceWatch AS w, armoryItem AS a " +
                "WHERE w.armoryId=a.armoryId AND w.highlighted=? ORDER BY a.itemName;", priceWatchMapper, highlighted);
    }
}

