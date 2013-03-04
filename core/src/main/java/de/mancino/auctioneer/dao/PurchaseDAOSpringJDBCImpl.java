package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.DataType.varChar;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.PurchaseOrderId.purchaseOrderId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.Purchase;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class PurchaseDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements PurchaseDAO {
    private static final long serialVersionUID = 1L;


    private final SerializableParameterizedRowMapper<Purchase> PurchaseMapper = new SerializableParameterizedRowMapper<Purchase>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Purchase mapRow(ResultSet rs, int rowNum) throws SQLException {
            Purchase purchase = new Purchase();
            purchase.setPurchaseOrderId(purchaseOrderId(rs.getInt("purchaseOrderId")));
            purchase.setTimeInMilliseconds(rs.getLong("timeInMilliseconds"));
            purchase.setItemCount(rs.getInt("itemCount"));
            purchase.setPrice(currency(rs.getLong("price")));
            purchase.setBuyerName(rs.getString("buyerName"));
            return purchase;
        }
    };


    public PurchaseDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public PurchaseDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("Purchase")
                .dataTypes(integer("purchaseOrderId").notNull(),
                        bigInteger("price").notNull(),
                        integer("itemCount").notNull(),
                        bigInteger("timeInMilliseconds").notNull(),
                        varChar("buyerName", 35).notNull())
                        );
    }

    @Override
    public Purchase insert(final Purchase purchase) {
        final String INSERT_STATEMENT = "INSERT INTO " +
                "Purchase (purchaseOrderId, price, itemCount, timeInMilliseconds, buyerName) " +
                "VALUES (?,?,?,?,?)";
        getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                purchase.getPurchaseOrderId().toInt(),
                purchase.getPrice().toLong(),
                purchase.getItemCount(),
                purchase.getTimeInMilliseconds(),
                purchase.getBuyerName());
        PurchaseOrderDAOSpringJDBCImpl.PurchaseOrderProxy.invalidatePurchases(purchase.getPurchaseOrderId());
        return purchase;
    }

    @Override
    public void deleteAllByPurchaseOrderId(final PurchaseOrderId PurchaseOrderId) {
        getSimpleJdbcTemplate().update("DELETE FROM Purchase WHERE purchaseOrderId=?",
                PurchaseOrderId.toInt());
    }

    @Override
    public List<Purchase> getAllByPurchaseOrderId(final PurchaseOrderId purchaseOrderId) {
        final List<Purchase> purchases = getSimpleJdbcTemplate().query(
                "SELECT purchaseOrderId, price, itemCount, timeInMilliseconds, buyerName " +
                "FROM Purchase WHERE purchaseOrderId=?", PurchaseMapper, purchaseOrderId.toInt());
        if (purchases==null) {
            return new ArrayList<Purchase>();
        } else {
            return purchases;
        }
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM Purchase");
    }
}
