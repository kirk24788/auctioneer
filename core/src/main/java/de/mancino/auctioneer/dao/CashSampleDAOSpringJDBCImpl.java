package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;

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
import de.mancino.auctioneer.dto.CashSample;
import de.mancino.auctioneer.dto.components.CharacterId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class CashSampleDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements CashSampleDAO {
    private static final long serialVersionUID = 1L;

    private final SerializableParameterizedRowMapper<CashSample> cashSampleMapper = new SerializableParameterizedRowMapper<CashSample>() {
        private static final long serialVersionUID = 1L;

        @Override
        public CashSample mapRow(ResultSet rs, int rowNum) throws SQLException {
            CashSample cashSample = new CashSample(new CharacterId(rs.getInt("characterId")), 
                    rs.getLong("timeInMilliseconds"),
                    new Currency(rs.getLong("cash")));
            return cashSample;
        }
    };


    public CashSampleDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public CashSampleDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("cashSample")
                .dataTypes(bigInteger("id").autoIncrement(),
                        integer("characterId").notNull(),
                        bigInteger("cash").notNull(),
                        bigInteger("timeInMilliseconds").notNull())
                        .primaryKeys("id"));
    }


    @Override
    public CashSample insert(final CashSample cashSample) {
        final String INSERT_STATEMENT = "INSERT INTO " +
                "cashSample (characterId, cash, timeInMilliseconds) " +
                "VALUES (?,?,?)";
        ArmoryCharacterDAOSpringJDBCImpl.ArmoryCharacterProxy.invalidateCashSamples(cashSample.getCharacterId());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, cashSample.getCharacterId().toInt());
                    ps.setLong(2, cashSample.getCash().toLong());
                    ps.setLong(3, cashSample.getTimeInMilliseconds());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            long id = keyHolder.getKey().longValue();
            cashSample.setId(id);
            return cashSample;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    cashSample.getCharacterId().toInt(),
                    cashSample.getCash().toLong(),
                    cashSample.getTimeInMilliseconds());
            long id = getSimpleJdbcTemplate().queryForLong("CALL IDENTITY();");
            cashSample.setId(id);
            return cashSample;
        }
    }

    @Override
    public void delete(CashSample cashSample) {
        ArmoryCharacterDAOSpringJDBCImpl.ArmoryCharacterProxy.invalidateCashSamples(cashSample.getCharacterId());
        getSimpleJdbcTemplate().update("DELETE FROM cashSample WHERE id=?",
                cashSample.getId());

    }

    @Override
    public void deleteAllByCharacterId(CharacterId characterId) {
        ArmoryCharacterDAOSpringJDBCImpl.ArmoryCharacterProxy.invalidateCashSamples(characterId);
        getSimpleJdbcTemplate().update("DELETE FROM cashSample WHERE characterId=?",
                characterId.toInt());

    }

    @Override
    public void deleteAllByMaxTimestamp(long maxTimestamp) {
        ArmoryCharacterDAOSpringJDBCImpl.ArmoryCharacterProxy.invalidateAllCashSamples();
        getSimpleJdbcTemplate().update("DELETE FROM cashSample WHERE timeInMilliseconds<?",
                maxTimestamp);

    }

    @Override
    public CashSample getById(long id) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, characterId, cash, timeInMilliseconds " +
                "FROM cashSample WHERE id=?",
                cashSampleMapper, id);
    }

    @Override
    public List<CashSample> getAllByCharacterId(CharacterId characterId) {
        final List<CashSample> cashSamples = getSimpleJdbcTemplate().query("SELECT id, characterId, cash, timeInMilliseconds " +
                "FROM cashSample WHERE characterId=? ORDER BY timeInMilliseconds", cashSampleMapper, characterId.toInt());
        if (cashSamples==null) {
            return new ArrayList<CashSample>();
        } else {
            return cashSamples;
        }
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM cashSample");
    }

}
