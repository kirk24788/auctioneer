package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.FarmStrategyId.farmStrategyId;

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
import de.mancino.auctioneer.dto.FarmStrategyProfit;
import de.mancino.auctioneer.dto.components.FarmStrategyId;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class FarmStrategyProfitDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements FarmStrategyProfitDAO {
    private static final long serialVersionUID = 1L;


    private final SerializableParameterizedRowMapper<FarmStrategyProfit> farmStrategyProfitMapper = new SerializableParameterizedRowMapper<FarmStrategyProfit>() {
        private static final long serialVersionUID = 1L;

        @Override
        public FarmStrategyProfit mapRow(ResultSet rs, int rowNum) throws SQLException {
            FarmStrategyProfit farmStrategyProfit = new FarmStrategyProfit(farmStrategyId(rs.getInt("farmStrategyId")));
            farmStrategyProfit.setId(rs.getLong("id"));
            farmStrategyProfit.setProfitTimestamp(rs.getLong("timestamp"));
            farmStrategyProfit.setMedianSalePrice(currency(rs.getLong("medianSalePrice")));
            farmStrategyProfit.setMinSalePrice(currency(rs.getLong("minSalePrice")));
            farmStrategyProfit.setSalePriceSampleSize(rs.getInt("salePriceSampleSize"));
            return farmStrategyProfit;
        }
    };

    public FarmStrategyProfitDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public FarmStrategyProfitDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("farmStrategyProfit")
                .dataTypes(
                        bigInteger("id").autoIncrement(),
                        integer("farmStrategyId").notNull(),
                        bigInteger("timestamp").notNull(),
                        bigInteger("medianSalePrice").notNull(),
                        bigInteger("minSalePrice").notNull(),
                        integer("salePriceSampleSize").notNull())
                        .primaryKeys("id"));
    }

    @Override
    public FarmStrategyProfit insert(final FarmStrategyProfit farmStrategyProfit) {
        final String INSERT_STATEMENT = "INSERT INTO farmStrategyProfit "
                + "(farmStrategyId, timestamp,  medianSalePrice, minSalePrice, salePriceSampleSize) "
                + "VALUES (?, ?, ?, ?, ?)";
        FarmStrategyDAOSpringJDBCImpl.FarmStrategyProxy.invalidateFarmStrategyProfits(farmStrategyProfit.getFarmStrategyId());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, farmStrategyProfit.getFarmStrategyId().toInt());
                    ps.setLong(2, farmStrategyProfit.getProfitTimestamp());
                    ps.setLong(3, farmStrategyProfit.getMedianSalePrice().toLong());
                    ps.setLong(4, farmStrategyProfit.getMinSalePrice().toLong());
                    ps.setInt(5, farmStrategyProfit.getSalePriceSampleSize());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            long id = keyHolder.getKey().longValue();
            farmStrategyProfit.setId(id);
            return farmStrategyProfit;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    farmStrategyProfit.getFarmStrategyId().toInt(),
                    farmStrategyProfit.getProfitTimestamp(),
                    farmStrategyProfit.getMedianSalePrice().toLong(),
                    farmStrategyProfit.getMinSalePrice().toLong(),
                    farmStrategyProfit.getSalePriceSampleSize()
                    );
            long id = getSimpleJdbcTemplate().queryForLong("CALL IDENTITY();");
            farmStrategyProfit.setId(id);
            return farmStrategyProfit;
        }
    }

    @Override
    public void delete(final FarmStrategyProfit farmStrategyProfit) {
        FarmStrategyDAOSpringJDBCImpl.FarmStrategyProxy.invalidateFarmStrategyProfits(farmStrategyProfit.getFarmStrategyId());
        getSimpleJdbcTemplate().update("DELETE FROM farmStrategyProfit WHERE id=?", farmStrategyProfit.getId());
    }

    @Override
    public void deleteAllByFarmStrategyId(final FarmStrategyId farmStrategyId) {
        FarmStrategyDAOSpringJDBCImpl.FarmStrategyProxy.invalidateFarmStrategyProfits(farmStrategyId);
        getSimpleJdbcTemplate().update("DELETE FROM farmStrategyProfit WHERE farmStrategyId=?", farmStrategyId);
    }

    @Override
    public void deleteAllByMaxTimestamp(final long maxTimestamp) {
        SaleStrategyDAOSpringJDBCImpl.SaleStrategyProxy.invalidateAllSaleStrategyMargins();
        getSimpleJdbcTemplate().update("DELETE FROM farmStrategyProfit WHERE timestamp<?", maxTimestamp);
    }

    @Override
    public FarmStrategyProfit getById(final long id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, farmStrategyId, timestamp, "
                + "medianSalePrice, minSalePrice, salePriceSampleSize "
                + "FROM farmStrategyProfit "
                + "WHERE id=?;", farmStrategyProfitMapper, id);
    }

    @Override
    public List<FarmStrategyProfit> getAllByFarmStrategyId(final FarmStrategyId farmStrategyId) {
        return getSimpleJdbcTemplate().query("SELECT id, farmStrategyId, timestamp, "
                + "medianSalePrice, minSalePrice, salePriceSampleSize "
                + "FROM farmStrategyProfit "
                + "WHERE farmStrategyId=? "
                + "ORDER BY timestamp;", farmStrategyProfitMapper, farmStrategyId.toInt());
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM farmStrategyProfit");
    }

}
