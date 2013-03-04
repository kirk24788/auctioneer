/**
 *
 */
package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.SaleStrategyId.saleStrategyId;

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
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.SaleStrategyId;
import de.mancino.utils.SerializableParameterizedRowMapper;

/**
 * @author mario
 */
public class SaleStrategyMaterialDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements SaleStrategyMaterialDAO {
    private static final long serialVersionUID = 1L;

    class SaleStrategyMaterialProxy extends SaleStrategyMaterial {
        private final ArmoryItemDAO armoryItemDAO;
        private final ArmoryId armoryId;

        SaleStrategyMaterialProxy(final SaleStrategyId saleStrategyId, final int itemCount,
                final JdbcTemplate jdbcTemplate, final ArmoryId armoryId) {
            super(saleStrategyId, itemCount, null);
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

    private final SerializableParameterizedRowMapper<SaleStrategyMaterial> saleStrategyMaterialMapper = new SerializableParameterizedRowMapper<SaleStrategyMaterial>() {
        private static final long serialVersionUID = 1L;

        @Override
        public SaleStrategyMaterial mapRow(ResultSet rs, int rowNum) throws SQLException {
            SaleStrategyMaterial saleStrategyMaterial = new SaleStrategyMaterialProxy(
                    saleStrategyId(rs.getInt("saleStrategyId")),
                    rs.getInt("itemCount"),
                    getJdbcTemplate(),
                    armoryId(rs.getInt("item")) );
            saleStrategyMaterial.setId(rs.getLong("id"));
            return saleStrategyMaterial;
        }
    };

    public SaleStrategyMaterialDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public SaleStrategyMaterialDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("saleStrategyMaterial")
                .dataTypes(bigInteger("id").autoIncrement(),
                        integer("saleStrategyId").notNull(),
                        integer("itemCount").notNull(),
                        integer("item").notNull())
                        .primaryKeys("id"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SaleStrategyMaterial insert(final SaleStrategyMaterial saleStrategyMaterial) {
        final String INSERT_STATEMENT = "INSERT INTO " +
                "saleStrategyMaterial (saleStrategyId, itemCount, item) " +
                "VALUES (?,?,?)";
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateSaleStrategyMaterials(saleStrategyMaterial.getSaleStrategyId());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, saleStrategyMaterial.getSaleStrategyId().toInt());
                    ps.setLong(2, saleStrategyMaterial.getItemCount());
                    ps.setLong(3, saleStrategyMaterial.getItem().getArmoryId().toInt());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            long id = keyHolder.getKey().longValue();
            saleStrategyMaterial.setId(id);
            return saleStrategyMaterial;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    saleStrategyMaterial.getSaleStrategyId().toInt(),
                    saleStrategyMaterial.getItemCount(),
                    saleStrategyMaterial.getItem().getArmoryId().toInt());
            long id = getSimpleJdbcTemplate().queryForLong("CALL IDENTITY();");
            saleStrategyMaterial.setId(id);
            return saleStrategyMaterial;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(SaleStrategyMaterial saleStrategyMaterial) {
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateSaleStrategyMaterials(saleStrategyMaterial.getSaleStrategyId());
        getSimpleJdbcTemplate().update("DELETE FROM saleStrategyMaterial WHERE id=?", saleStrategyMaterial.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllBySaleStrategyId(SaleStrategyId saleStrategyId) {
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateSaleStrategyMaterials(saleStrategyId);
        getSimpleJdbcTemplate().update("DELETE FROM saleStrategyMaterial WHERE saleStrategyId=?", saleStrategyId.toInt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SaleStrategyMaterial getById(long id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, saleStrategyId, itemCount, item " +
                "FROM saleStrategyMaterial WHERE id=?", saleStrategyMaterialMapper, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SaleStrategyMaterial> getAllBySaleStrategyId( SaleStrategyId saleStrategyId) {
        final List<SaleStrategyMaterial> priceSamples = getSimpleJdbcTemplate().query(
                "SELECT id, saleStrategyId, itemCount, item " +
                "FROM saleStrategyMaterial WHERE saleStrategyId=?", saleStrategyMaterialMapper, saleStrategyId.toInt());
        if (priceSamples==null) {
            return new ArrayList<SaleStrategyMaterial>();
        } else {
            return priceSamples;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM saleStrategyMaterial");
    }

}
