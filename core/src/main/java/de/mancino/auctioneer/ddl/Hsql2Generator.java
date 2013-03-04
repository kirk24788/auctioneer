package de.mancino.auctioneer.ddl;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class Hsql2Generator extends SqlGenerator {
    public Hsql2Generator(final SimpleJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        jdbcTemplate.getJdbcOperations().execute("SET WRITE_DELAY 0;");
    }

    @Override
    protected String getCreateIfNotExistsCommand() {
        return "CREATE TABLE";
    }

    @Override
    protected boolean doesTableAlreadyExist(Table table) {
        try {
            jdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + table.tableName);
            return true;
        } catch (BadSqlGrammarException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected  String getAutoIncrementString() {
        return "GENERATED BY DEFAULT AS IDENTITY";
    }
}
