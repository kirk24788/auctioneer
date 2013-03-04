package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.blob;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.DataType.varChar;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryIcon.armoryIcon;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.ItemName.itemName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class ArmoryItemDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements ArmoryItemDAO {
    private static final long serialVersionUID = 1L;

    private final SerializableParameterizedRowMapper<ArmoryItem> armoryItemMapper = new SerializableParameterizedRowMapper<ArmoryItem>() {
        private static final long serialVersionUID = 1L;

        @Override
        public ArmoryItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArmoryItem armoryItem = new ArmoryItem();
            armoryItem.setArmoryId(armoryId(rs.getInt("armoryId")));
            armoryItem.setItemName(itemName(rs.getString("itemName")));
            armoryItem.setArmoryIcon(armoryIcon((rs.getBytes("armoryIcon"))));
            return armoryItem;
        }
    };

    public ArmoryItemDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public ArmoryItemDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("armoryItem")
                    .dataTypes(integer("armoryId").notNull(),
                               varChar("itemName").size(100).notNull(),
                               blob("armoryIcon").notNull())
                    .primaryKeys("armoryId"));
    }

    @Override
    public ArmoryItem insert(final ArmoryItem armoryItem) {
        getSimpleJdbcTemplate().update("INSERT INTO armoryItem (armoryId, itemName, armoryIcon) VALUES (?,?,?)",
                armoryItem.getArmoryId().toInt(),
                armoryItem.getItemName().toString(),
                armoryItem.getArmoryIcon().toByteArray());
        return armoryItem;
    }

    @Override
    public void update(final ArmoryItem armoryItem) {
        getSimpleJdbcTemplate().update("UPDATE armoryItem SET itemName=?, armoryIcon=? WHERE armoryId=?",
                armoryItem.getItemName().toString(),
                armoryItem.getArmoryIcon().toByteArray(),
                armoryItem.getArmoryId().toInt());
    }

    @Override
    public void delete(final ArmoryItem armoryItem) {
        getSimpleJdbcTemplate().update("DELETE FROM armoryItem WHERE armoryId=?",
                armoryItem.getArmoryId().toInt());
    }

    @Override
    public ArmoryItem getByArmoryId(final ArmoryId armoryId) {
        return getSimpleJdbcTemplate().queryForObject("SELECT armoryId, itemName, armoryIcon " +
                "FROM armoryItem WHERE armoryId=?",
                armoryItemMapper, armoryId.toInt());
    }

    @Override
    public ArmoryItem getByItemName(final ItemName itemName) {
        return getSimpleJdbcTemplate().queryForObject("SELECT armoryId, itemName, armoryIcon " +
                "FROM armoryItem WHERE itemName=?",
                armoryItemMapper, itemName.toString());
    }

    @Override
    public List<ArmoryItem> getAll() {
        return getSimpleJdbcTemplate().query("SELECT armoryId, itemName, armoryIcon " +
                "FROM armoryItem ORDER BY itemName", armoryItemMapper);
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM armoryItem");
    }
}
