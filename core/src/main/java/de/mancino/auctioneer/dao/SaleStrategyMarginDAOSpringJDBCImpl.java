package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.SaleStrategyId.saleStrategyId;

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
import de.mancino.auctioneer.dto.SaleStrategyMargin;
import de.mancino.auctioneer.dto.components.SaleStrategyId;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class SaleStrategyMarginDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements SaleStrategyMarginDAO {
    private static final long serialVersionUID = 1L;


    private final SerializableParameterizedRowMapper<SaleStrategyMargin> saleStrategyMarginMapper = new SerializableParameterizedRowMapper<SaleStrategyMargin>() {
        private static final long serialVersionUID = 1L;

        @Override
        public SaleStrategyMargin mapRow(ResultSet rs, int rowNum) throws SQLException {
            SaleStrategyMargin saleStrategyMaterial = new SaleStrategyMargin(saleStrategyId(rs.getInt("saleStrategyId")));
            saleStrategyMaterial.setId(rs.getLong("id"));
            saleStrategyMaterial.setMarginTimestamp(rs.getLong("marginTimestamp"));
            saleStrategyMaterial.setMedianMaterialCost(currency(rs.getLong("medianMaterialCost")));
            saleStrategyMaterial.setMinMaterialCost(currency(rs.getLong("minMaterialCost")));
            saleStrategyMaterial.setMedianSalePrice(currency(rs.getLong("medianSalePrice")));
            saleStrategyMaterial.setMinSalePrice(currency(rs.getLong("minSalePrice")));
            saleStrategyMaterial.setMaterialCostSampleSize(rs.getInt("materialCostSampleSize"));
            saleStrategyMaterial.setSalePriceSampleSize(rs.getInt("salePriceSampleSize"));
            return saleStrategyMaterial;
        }
    };

    public SaleStrategyMarginDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public SaleStrategyMarginDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("saleStrategyMargin")
                .dataTypes(
                        bigInteger("id").autoIncrement(),
                        integer("saleStrategyId").notNull(),
                        bigInteger("marginTimestamp").notNull(),
                        bigInteger("medianMaterialCost").notNull(),
                        bigInteger("minMaterialCost").notNull(),
                        integer("materialCostSampleSize").notNull(),
                        bigInteger("medianSalePrice").notNull(),
                        bigInteger("minSalePrice").notNull(),
                        integer("salePriceSampleSize").notNull())
                        .primaryKeys("id"));
    }

    @Override
    public SaleStrategyMargin insert(final SaleStrategyMargin saleStrategyMargin) {
        final String INSERT_STATEMENT = "INSERT INTO saleStrategyMargin "
                + "(saleStrategyId, marginTimestamp, medianMaterialCost, minMaterialCost, "
                + "materialCostSampleSize, medianSalePrice, minSalePrice, salePriceSampleSize) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateSaleStrategyMargins(saleStrategyMargin.getSaleStrategyId());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, saleStrategyMargin.getSaleStrategyId().toInt());
                    ps.setLong(2, saleStrategyMargin.getMarginTimestamp());
                    ps.setLong(3, saleStrategyMargin.getMedianMaterialCost().toLong());
                    ps.setLong(4, saleStrategyMargin.getMinMaterialCost().toLong());
                    ps.setInt(5, saleStrategyMargin.getMaterialCostSampleSize());
                    ps.setLong(6, saleStrategyMargin.getMedianSalePrice().toLong());
                    ps.setLong(7, saleStrategyMargin.getMinSalePrice().toLong());
                    ps.setInt(8, saleStrategyMargin.getSalePriceSampleSize());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            long id = keyHolder.getKey().longValue();
            saleStrategyMargin.setId(id);
            return saleStrategyMargin;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    saleStrategyMargin.getSaleStrategyId().toInt(),
                    saleStrategyMargin.getMarginTimestamp(),
                    saleStrategyMargin.getMedianMaterialCost().toLong(),
                    saleStrategyMargin.getMinMaterialCost().toLong(),
                    saleStrategyMargin.getMaterialCostSampleSize(),
                    saleStrategyMargin.getMedianSalePrice().toLong(),
                    saleStrategyMargin.getMinSalePrice().toLong(),
                    saleStrategyMargin.getSalePriceSampleSize()
                    );
            long id = getSimpleJdbcTemplate().queryForLong("CALL IDENTITY();");
            saleStrategyMargin.setId(id);
            return saleStrategyMargin;
        }
    }


    @Override
    public void delete(SaleStrategyMargin saleStrategyMargin) {
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateSaleStrategyMargins(saleStrategyMargin.getSaleStrategyId());
        getSimpleJdbcTemplate().update("DELETE FROM saleStrategyMargin WHERE id=?", saleStrategyMargin.getId());
    }


    @Override
    public void deleteAllBySaleStrategyId(SaleStrategyId saleStrategyId) {
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateSaleStrategyMargins(saleStrategyId);
        getSimpleJdbcTemplate().update("DELETE FROM saleStrategyMargin WHERE saleStrategyId=?", saleStrategyId.toInt());
    }


    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateAllSaleStrategyMargins();
        getSimpleJdbcTemplate().update("DELETE FROM saleStrategyMargin WHERE marginTimestamp<?",
                maxTimestamp);
    }


    @Override
    public SaleStrategyMargin getById(long id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, saleStrategyId, marginTimestamp, "
                + "medianMaterialCost, minMaterialCost, materialCostSampleSize, medianSalePrice, minSalePrice, "
                + "salePriceSampleSize "
                + "FROM saleStrategyMargin "
                + "WHERE id=?;", saleStrategyMarginMapper, id);
    }


    @Override
    public List<SaleStrategyMargin> getAllBySaleStrategyId(SaleStrategyId saleStrategyId) {
        return getSimpleJdbcTemplate().query("SELECT id, saleStrategyId, marginTimestamp, "
                + "medianMaterialCost, minMaterialCost, materialCostSampleSize, medianSalePrice, minSalePrice, "
                + "salePriceSampleSize "
                + "FROM saleStrategyMargin "
                + "WHERE saleStrategyId=? "
                + "ORDER BY marginTimestamp;", saleStrategyMarginMapper, saleStrategyId.toInt());
    }


    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM saleStrategyMargin");
    }

}
