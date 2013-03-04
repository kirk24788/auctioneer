package de.mancino.auctioneer.ddl;

public class DataType {
    public enum Type {
        VARCHAR,
        INTEGER,
        BIG_INTEGER,
        BLOB,
        BOOLEAN;
    }
    public final Type type;
    public final String rowName;
    public final boolean isNullable;
    public final int size;
    public final boolean autoIncrement;

    DataType(final Type type, final String rowName, final boolean isNullable, final int size, final boolean autoIncrement) {
        this.type = type;
        this.rowName = rowName;
        this.isNullable = isNullable;
        this.size = size;
        this.autoIncrement = autoIncrement;
    }

    static final DataType dataType(final Type type, final String rowName, final int size, final boolean autoIncrement) {
        return new DataType(type, rowName, true, size, autoIncrement);
    }

    public DataType notNull() {
        return new DataType(type, rowName, false, size, autoIncrement);
    }

    public DataType nullable() {
        return new DataType(type, rowName, true, size, autoIncrement);
    }
    
    public DataType autoIncrement() {
        return new DataType(type, rowName, isNullable, size, true);
    }
    
    
    public DataType size(final int size) {
        return new DataType(type, rowName, isNullable, size, autoIncrement);
    }

    public static final DataType varChar(final String rowName) {
        return dataType(Type.VARCHAR, rowName, 100, false);
    }

    public static final DataType varChar(final String rowName, final int length) {
        return dataType(Type.VARCHAR, rowName, length, false);
    }

    public static final DataType bool(final String rowName) {
        return dataType(Type.BOOLEAN, rowName, 1, false);
    }

    public static final DataType integer(final String rowName) {
        return dataType(Type.INTEGER, rowName, 4, false);
    }

    public static final DataType bigInteger(final String rowName) {
        return dataType(Type.BIG_INTEGER, rowName, 8, false);
    }

    public static final DataType blob(final String rowName) {
        return dataType(Type.BLOB, rowName, 0, false);
    }
}
