package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.PriceWatchId.priceWatchId;

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
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.components.PriceWatchId;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class PriceSampleDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements PriceSampleDAO {
    private static final long serialVersionUID = 1L;


    private final SerializableParameterizedRowMapper<PriceSample> priceSampleMapper = new SerializableParameterizedRowMapper<PriceSample>() {
        private static final long serialVersionUID = 1L;

        @Override
        public PriceSample mapRow(ResultSet rs, int rowNum) throws SQLException {
            PriceSample priceSample = new PriceSample(priceWatchId(rs.getInt("priceWatchId")), rs.getLong("timeInMilliseconds"));
            priceSample.setId(rs.getLong("id"));
            priceSample.setMedianPrice(currency(rs.getLong("medianPrice")));
            priceSample.setAveragePrice(currency((rs.getLong("averagePrice"))));
            priceSample.setMinimumPrice(currency((rs.getLong("minimumPrice"))));
            priceSample.setSampleSize((rs.getInt("sampleSize")));
            return priceSample;
        }
    };


    public PriceSampleDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public PriceSampleDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("priceSample")
                .dataTypes(bigInteger("id").autoIncrement(),
                        integer("priceWatchId").notNull(),
                        bigInteger("medianPrice").notNull(),
                        bigInteger("averagePrice").notNull(),
                        bigInteger("minimumPrice").notNull(),
                        bigInteger("timeInMilliseconds").notNull(),
                        integer("sampleSize").notNull())
                        .primaryKeys("id"));
    }

    @Override
    public PriceSample insert(final PriceSample priceSample) {
        final String INSERT_STATEMENT = "INSERT INTO " +
                                "priceSample (priceWatchId, medianPrice, averagePrice, minimumPrice, sampleSize, timeInMilliseconds) " +
                                "VALUES (?,?,?,?,?,?)";
        PriceWatchDAOSpringJDBCImpl.PriceWatchProxy.invalidatePriceSamples(priceSample.getPriceWatchId());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                        ps.setInt(1, priceSample.getPriceWatchId().toInt());
                        ps.setLong(2, priceSample.getMedianPrice().toLong());
                        ps.setLong(3, priceSample.getAveragePrice().toLong());
                        ps.setLong(4, priceSample.getMinimumPrice().toLong());
                        ps.setLong(5, priceSample.getSampleSize());
                        ps.setLong(6, priceSample.getTimeInMilliseconds());
                        return ps;
                    }
                };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            long id = keyHolder.getKey().longValue();
            priceSample.setId(id);
            return priceSample;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    priceSample.getPriceWatchId().toInt(),
                    priceSample.getMedianPrice().toLong(),
                    priceSample.getAveragePrice().toLong(),
                    priceSample.getMinimumPrice().toLong(),
                    priceSample.getSampleSize(),
                    priceSample.getTimeInMilliseconds());
            long id = getSimpleJdbcTemplate().queryForLong("CALL IDENTITY();");
            priceSample.setId(id);
            return priceSample;
        }
    }

    @Override
    public void delete(final PriceSample priceSample) {
        PriceWatchDAOSpringJDBCImpl.PriceWatchProxy.invalidatePriceSamples(priceSample.getPriceWatchId());
        getSimpleJdbcTemplate().update("DELETE FROM priceSample WHERE id=?",
                priceSample.getId());
    }

    @Override
    public void deleteAllByPriceWatchId(final PriceWatchId priceWatchId) {
        PriceWatchDAOSpringJDBCImpl.PriceWatchProxy.invalidatePriceSamples(priceWatchId);
        getSimpleJdbcTemplate().update("DELETE FROM priceSample WHERE priceWatchId=?",
                priceWatchId.toInt());
    }

    @Override
    public void deleteAllByMaxTimestamp(final long maxTimestamp) {
        PriceWatchDAOSpringJDBCImpl.PriceWatchProxy.invalidateAllPriceSamples();
        getSimpleJdbcTemplate().update("DELETE FROM priceSample WHERE timeInMilliseconds<?",
                maxTimestamp);
    }

    @Override
    public PriceSample getById(final long id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, priceWatchId, medianPrice, averagePrice, minimumPrice, sampleSize, timeInMilliseconds " +
                "FROM priceSample WHERE id=?",
                priceSampleMapper, id);
    }

    @Override
    public List<PriceSample> getAllByPriceWatchId(final PriceWatchId priceWatchId) {
        final List<PriceSample> priceSamples = getSimpleJdbcTemplate().query("SELECT id, priceWatchId, medianPrice, averagePrice, minimumPrice, sampleSize, timeInMilliseconds " +
                "FROM priceSample WHERE priceWatchId=? ORDER BY timeInMilliseconds", priceSampleMapper, priceWatchId.toInt());
        if (priceSamples==null) {
            return new ArrayList<PriceSample>();
        } else {
            return priceSamples;
        }
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM priceSample");
    }
}
