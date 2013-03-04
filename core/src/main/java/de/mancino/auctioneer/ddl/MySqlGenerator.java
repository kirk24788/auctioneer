package de.mancino.auctioneer.ddl;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class MySqlGenerator extends SqlGenerator {

    public MySqlGenerator(final SimpleJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        
    }

    @Override
    protected String getCreateIfNotExistsCommand() {
        return "CREATE TABLE IF NOT EXISTS";
    }

    @Override
    protected boolean doesTableAlreadyExist(Table table) {
        return false;
    }}
