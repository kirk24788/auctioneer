package de.mancino.auctioneer.ddl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table {
    public final String tableName;
    public final List<DataType> dataTypes;
    public final List<String> uniqueKeys;
    public final List<String> primaryKeys;

    Table(final String tableName) {
        this(tableName, Collections.unmodifiableList(new ArrayList<DataType>()),
                Collections.unmodifiableList(new ArrayList<String>()),
                Collections.unmodifiableList(new ArrayList<String>()));
    }

    Table(final String tableName, final List<DataType> dataTypes, final List<String> uniqueKeys,
            final List<String>primaryKeys) {
        this.tableName = tableName;
        this.dataTypes = dataTypes;
        this.uniqueKeys = uniqueKeys;
        this.primaryKeys = primaryKeys;
    }

    @SafeVarargs
    static <T> List<T> varargToList(final T ... varargs) {
        final List<T> list = new ArrayList<T>();
        for(final T vararg : varargs) {
            list.add(vararg);
        }
        return Collections.unmodifiableList(list);
    }

    public static Table table(final String tableName) {
        return new Table(tableName);
    }

    public Table dataTypes(final DataType ... dataTypes) {
        return new Table(tableName, varargToList(dataTypes), uniqueKeys, primaryKeys);
    }

    public Table uniqueKeys(final String ... uniqueKeys) {
        return new Table(tableName, dataTypes, varargToList(uniqueKeys), primaryKeys);
    }

    public Table primaryKeys(final String ... primaryKeys) {
        return new Table(tableName, dataTypes, uniqueKeys, varargToList(primaryKeys));
    }
}
