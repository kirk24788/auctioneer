package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.bool;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.DataType.varChar;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.PurchaseOrderId.purchaseOrderId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.Purchase;
import de.mancino.auctioneer.dto.PurchaseOrder;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.auctioneer.strategies.BuyoutStrategy;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class PurchaseOrderDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements PurchaseOrderDAO {
    private static final long serialVersionUID = 1L;

    static class PurchaseOrderProxy extends PurchaseOrder {
        private static final long serialVersionUID = 200L;

        private final ArmoryItemDAO armoryItemDAO;
        private final PurchaseDAOSpringJDBCImpl purchaseDAO;
        private final ArmoryId armoryId;
        private static final ConcurrentMap<PurchaseOrderId, List<Purchase>> purchaseMap = new ConcurrentHashMap<PurchaseOrderId, List<Purchase>>();

        PurchaseOrderProxy(final JdbcTemplate jdbcTemplate, final ArmoryId armoryId) {
            this.armoryItemDAO = new ArmoryItemDAOSpringJDBCImpl(jdbcTemplate);
            this.purchaseDAO = new PurchaseDAOSpringJDBCImpl(jdbcTemplate);
            this.armoryId = armoryId;
            super.setPurchases(null);
            super.setArmoryItem(null);
        }

        @Override
        public ArmoryItem getArmoryItem() {
            if (super.getArmoryItem()==null) {
                super.setArmoryItem(armoryItemDAO.getByArmoryId(armoryId));
            }
            return super.getArmoryItem();
        }

        @Override
        public synchronized List<Purchase> getPurchases() {
            List<Purchase> purchases = purchaseMap.get(getId());
            if (purchases==null) {
                purchases = purchaseDAO.getAllByPurchaseOrderId(getId());
                purchaseMap.put(getId(), purchases);
            }
            return purchases;
        }

        static void invalidatePurchases(final PurchaseOrderId id) {
            purchaseMap.remove(id);
        }
    }


    public PurchaseOrderDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("purchaseOrder")
                .dataTypes(integer("id").autoIncrement(),
                        bool("active").notNull(),
                        integer("armoryId").notNull(),
                        integer("itemCount").notNull(),
                        bigInteger("maxPrice").notNull(),
                        varChar("strategy", 30).notNull(),
                        bigInteger("timeInMilliseconds").notNull())
                        .primaryKeys("id"));
    }

    private final SerializableParameterizedRowMapper<PurchaseOrder> priceWatchMapper = new SerializableParameterizedRowMapper<PurchaseOrder>() {
        private static final long serialVersionUID = 1L;

        @Override
        public PurchaseOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
            PurchaseOrder purchaseOrder = new PurchaseOrderProxy(getJdbcTemplate(), armoryId(rs.getInt("armoryId")));
            purchaseOrder.setId(purchaseOrderId(rs.getInt("id")));
            purchaseOrder.setActive(rs.getBoolean("active"));
            purchaseOrder.setItemCount(rs.getInt("itemCount"));
            purchaseOrder.setMaxPrice(currency(rs.getLong("maxPrice")));
            purchaseOrder.setStrategy(BuyoutStrategy.valueOf(rs.getString("strategy")));
            purchaseOrder.setTimeInMilliseconds(rs.getLong("timeInMilliseconds"));
            return purchaseOrder;
        }
    };

    @Override
    public PurchaseOrder insert(final PurchaseOrder purchaseOrder) {
        final String INSERT_STATEMENT = "INSERT INTO purchaseOrder " +
                "(active, itemCount, maxPrice, strategy, timeInMilliseconds, armoryId) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setBoolean(1, purchaseOrder.isActive());
                    ps.setInt(2, purchaseOrder.getItemCount());
                    ps.setLong(3, purchaseOrder.getMaxPrice().toLong());
                    ps.setString(4, purchaseOrder.getStrategy().name());
                    ps.setLong(5, purchaseOrder.getTimeInMilliseconds());
                    ps.setLong(6, purchaseOrder.getArmoryItem().getArmoryId().toInt());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            int id = keyHolder.getKey().intValue();
            purchaseOrder.setId(new PurchaseOrderId(id));
            return purchaseOrder;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    purchaseOrder.isActive(),
                    purchaseOrder.getItemCount(),
                    purchaseOrder.getMaxPrice().toLong(),
                    purchaseOrder.getStrategy().name(),
                    purchaseOrder.getTimeInMilliseconds(),
                    purchaseOrder.getArmoryItem().getArmoryId().toInt());
            int id = getSimpleJdbcTemplate().queryForInt("CALL IDENTITY();");
            purchaseOrder.setId(new PurchaseOrderId(id));
            return purchaseOrder;
        }
    }

    @Override
    public void delete(final PurchaseOrder purchaseOrder) {
        getSimpleJdbcTemplate().update("DELETE FROM purchaseOrder WHERE id=?",
                purchaseOrder.getId().toInt());
    }

    @Override
    public List<PurchaseOrder> getAll() {
        return getSimpleJdbcTemplate().query(
                "SELECT o.id, o.active, o.itemCount, o.maxPrice, o.strategy, o.timeInMilliseconds, o.armoryId " +
                "FROM purchaseOrder AS o, armoryItem AS a " +
                "WHERE o.armoryId=a.armoryId ORDER BY a.itemName;", priceWatchMapper);
    }
    @Override
    public List<PurchaseOrder> getAllByActiveStatus(final boolean isActive) {
        return getSimpleJdbcTemplate().query(
                "SELECT o.id, o.active, o.itemCount, o.maxPrice, o.strategy, o.timeInMilliseconds, o.armoryId " +
                "FROM purchaseOrder AS o, armoryItem AS a " +
                "WHERE o.armoryId=a.armoryId AND o.active=? ORDER BY a.itemName;", priceWatchMapper, isActive);
    }

    @Override
    public PurchaseOrder getByPurchaseOrderId(final PurchaseOrderId purchaseOrderId) {
        return getSimpleJdbcTemplate().queryForObject(
                "SELECT id, active, itemCount, maxPrice, strategy, timeInMilliseconds, armoryId " +
                "FROM purchaseOrder WHERE id=?",
                priceWatchMapper, purchaseOrderId.toInt());
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM purchaseOrder");
    }

    @Override
    public void setActiveStatus(final PurchaseOrder purchaseOrder, final boolean isActive) {
        getSimpleJdbcTemplate().update("UPDATE purchaseOrder SET active=? WHERE id=?",
                isActive, purchaseOrder.getId().toInt());
    }

}

