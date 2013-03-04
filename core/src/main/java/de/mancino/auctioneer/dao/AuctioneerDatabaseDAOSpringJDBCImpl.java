/**
 * 
 */
package de.mancino.auctioneer.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author mmancino
 */
public class AuctioneerDatabaseDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements AuctioneerDatabaseDAO {
    private static final long serialVersionUID = 1L;

    /*
Sum up the data_length + index_length is equal to the total table size.

data_length – store the real data.
index_length – store the table index.
Here’s the SQL script to list out the entire databases size

SELECT table_schema "Data Base Name", SUM( data_length + index_length) / 1024 / 1024 
"Data Base Size in MB" FROM information_schema.TABLES GROUP BY table_schema ;



Another SQL script to list out one database size, and each tables size in detail

SELECT TABLE_NAME, table_rows, data_length, index_length, 
round(((data_length + index_length) / 1024 / 1024),2) "Size in MB"
FROM information_schema.TABLES WHERE table_schema = "schema_name";
 */

    private String databaseName;
    
    public AuctioneerDatabaseDAOSpringJDBCImpl(final String databaseName) {
        this.databaseName = databaseName;
    }

    public AuctioneerDatabaseDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getDatabaseSize() {
        return getSimpleJdbcTemplate().queryForLong("SELECT SUM( data_length + index_length) FROM information_schema.TABLES WHERE table_schema=?", databaseName);
    }

}
