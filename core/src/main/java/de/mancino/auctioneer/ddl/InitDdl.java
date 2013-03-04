package de.mancino.auctioneer.ddl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class InitDdl {
    private final SimpleJdbcTemplate jdbcTemplate;
    private final SqlGenerator sqlGenerator;

    public InitDdl(final DataSource dataSource, final DdlDialect dialect) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        switch(dialect) {
        case HSQL2:
            sqlGenerator = new Hsql2Generator(jdbcTemplate);
            break;
        case MYSQL:
            sqlGenerator = new MySqlGenerator(jdbcTemplate);
            break;

        default:
            throw new RuntimeException("Dialect not yet implemented!");
        }
    }

    public void createTableIfNotExists(final Table table) {
        sqlGenerator.createTableIfNotExists(table);
    }
}
