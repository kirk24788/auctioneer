package de.mancino.auctioneer.ddl;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public abstract class SqlGenerator {
    protected final SimpleJdbcTemplate jdbcTemplate;
    

    public SqlGenerator(final SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTableIfNotExists(final Table table) {
        if(!doesTableAlreadyExist(table)) {
            jdbcTemplate.getJdbcOperations().execute(getCreateStatement(table));
        }
    }

    protected String getCreateStatement(final Table table) {
        final StringBuffer sb = new StringBuffer();
        sb.append(getCreateIfNotExistsCommand()).append(" ").append(table.tableName).append(" (");
        boolean prefixWithComma = false;
        for(final DataType dataType : table.dataTypes) {
            if(!prefixWithComma) {
                prefixWithComma = true;
            } else {
                sb.append(", ");
            }
            sb.append(dataType.rowName).append(" ");
            sb.append(getDatyTypeString(dataType));
            if(!dataType.isNullable) {
                sb.append(" ").append(getNotNullString());
            }
            if(dataType.autoIncrement) {
                sb.append(" ").append(getAutoIncrementString());
            }
        }
        if(table.primaryKeys.size() > 0) {
            sb.append(", PRIMARY KEY (");
            prefixWithComma = false;
            for(final String primaryKey : table.primaryKeys) {
                if(!prefixWithComma) {
                    prefixWithComma = true;
                } else {
                    sb.append(" ,");
                }
                sb.append(primaryKey);
            }
            sb.append(") ");
        }
        if(table.uniqueKeys.size() > 0) {
            sb.append(", UNIQUE (");
            prefixWithComma = false;
            for(final String uniqueKey : table.uniqueKeys) {
                if(!prefixWithComma) {
                    prefixWithComma = true;
                } else {
                    sb.append(" ,");
                }
                sb.append(uniqueKey);
            }
            sb.append(") ");
        }
        sb.append(");");
        return sb.toString();
    }

    protected  String getAutoIncrementString() {
        return "AUTO_INCREMENT";
    }
    
    protected String getDatyTypeString(final DataType dataType) {
        StringBuffer sb = new StringBuffer();
        switch(dataType.type) {
        case BIG_INTEGER:
            sb.append("BIGINT");
            break;
        case BLOB:
            sb.append("BLOB");
            break;
        case INTEGER:
            sb.append("INT");
            break;
        case VARCHAR:
            sb.append("VARCHAR(").append(dataType.size).append(")");
            break;
        case BOOLEAN:
            sb.append("BOOLEAN");
            break;
        default:
            throw new RuntimeException("Data Type '" + dataType.type + "' not yet implemented in Generic Dialect!");
        }
        return sb.toString();
    }
    
    protected String getNotNullString() {
        return "NOT NULL";
    }

    protected String getCreateIfNotExistsCommand() {
        return "CREATE";
    }
    
    protected boolean doesTableAlreadyExist(final Table table) {
        return false;
    }
}
