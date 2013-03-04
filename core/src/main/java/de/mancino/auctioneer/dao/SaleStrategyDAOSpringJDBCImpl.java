package de.mancino.auctioneer.dao;

import static de.mancino.auctioneer.ddl.DataType.bigInteger;
import static de.mancino.auctioneer.ddl.DataType.integer;
import static de.mancino.auctioneer.ddl.Table.table;
import static de.mancino.auctioneer.dto.components.ArmoryId.armoryId;
import static de.mancino.auctioneer.dto.components.Currency.currency;
import static de.mancino.auctioneer.dto.components.SaleStrategyId.saleStrategyId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import de.mancino.auctioneer.ddl.InitDdl;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMargin;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.SaleStrategyId;
import de.mancino.utils.DAOCacheMap;
import de.mancino.utils.SerializableParameterizedRowMapper;

public class SaleStrategyDAOSpringJDBCImpl extends SimpleJdbcDaoSupport implements SaleStrategyDAO {
    private static final long serialVersionUID = 1;

    static class SaleStrategyProxy extends SaleStrategy {
        private static final DAOCacheMap<SaleStrategyId, List<SaleStrategyMaterial>,SaleStrategyMaterialDAO> materialMap =
                new DAOCacheMap<SaleStrategyId, List<SaleStrategyMaterial>,SaleStrategyMaterialDAO>() {
            @Override
            protected List<SaleStrategyMaterial> reload(SaleStrategyId key, SaleStrategyMaterialDAO dataStore) {
                return dataStore.getAllBySaleStrategyId(key);
            }
        };
        private static final DAOCacheMap<SaleStrategyId, List<SaleStrategyMargin>,SaleStrategyMarginDAO> marginMap =
                new DAOCacheMap<SaleStrategyId, List<SaleStrategyMargin>,SaleStrategyMarginDAO>() {
            @Override
            protected List<SaleStrategyMargin> reload(SaleStrategyId key, SaleStrategyMarginDAO dataStore) {
                return dataStore.getAllBySaleStrategyId(key);
            }
        };

        private static final long serialVersionUID = 100L;

        private final ArmoryItemDAO armoryItemDAO;
        private final ArmoryId productId;

        SaleStrategyProxy(final JdbcTemplate jdbcTemplate, final int productCount, final ArmoryId productId) {
            super(productCount, null);
            this.armoryItemDAO = new ArmoryItemDAOSpringJDBCImpl(jdbcTemplate);
            materialMap.setDataStore(new SaleStrategyMaterialDAOSpringJDBCImpl(jdbcTemplate));
            marginMap.setDataStore(new SaleStrategyMarginDAOSpringJDBCImpl(jdbcTemplate));
            this.productId = productId;
            super.setMaterials(null);
        }

        @Override
        public synchronized List<SaleStrategyMaterial> getMaterials() {
            return materialMap.get(super.getId());
        }

        @Override
        public List<SaleStrategyMargin> getMargins() {
            return marginMap.get(super.getId());
        }


        @Override
        public synchronized ArmoryItem getProduct() {
            if (super.getProduct()==null) {
                super.setProduct(armoryItemDAO.getByArmoryId(productId));
            }
            return super.getProduct();
        }

        static void invalidateSaleStrategyMaterials(final SaleStrategyId id) {
            materialMap.invalidate(id);
        }

        static void invalidateAllSaleStrategyMaterials() {
            materialMap.invalidateAll();
        }

        static void invalidateSaleStrategyMargins(final SaleStrategyId id) {
            marginMap.invalidate(id);
        }

        static void invalidateAllSaleStrategyMargins() {
            marginMap.invalidateAll();
        }
    }


    private final SerializableParameterizedRowMapper<SaleStrategy> saleStrategyMapper = new SerializableParameterizedRowMapper<SaleStrategy>() {
        private static final long serialVersionUID = 1L;

        @Override
        public SaleStrategy mapRow(ResultSet rs, int rowNum) throws SQLException {
            SaleStrategy saleStrategy = new SaleStrategyProxy(getJdbcTemplate(),
                    rs.getInt("productCount"),
                    armoryId(rs.getInt("product"))
                    );
            saleStrategy.setId(saleStrategyId(rs.getInt("id")));
            saleStrategy.setMarginTimestamp(rs.getLong("marginTimestamp"));
            saleStrategy.setAdditionalExpenses(currency(rs.getLong("additionalExpenses")));
            saleStrategy.setMedianMaterialCost(currency(rs.getLong("medianMaterialCost")));
            saleStrategy.setMinMaterialCost(currency(rs.getLong("minMaterialCost")));
            saleStrategy.setMedianSalePrice(currency(rs.getLong("medianSalePrice")));
            saleStrategy.setMinSalePrice(currency(rs.getLong("minSalePrice")));
            saleStrategy.setMaterialCostSampleSize(rs.getInt("materialCostSampleSize"));
            saleStrategy.setSalePriceSampleSize(rs.getInt("salePriceSampleSize"));
            return saleStrategy;
        }
    };

    public SaleStrategyDAOSpringJDBCImpl(final JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public SaleStrategyDAOSpringJDBCImpl(final InitDdl initDdl) {
        initDdl.createTableIfNotExists(
                table("saleStrategy")
                .dataTypes(
                        integer("id").autoIncrement(),
                        integer("productCount").notNull(),
                        integer("product").notNull(),
                        bigInteger("marginTimestamp").notNull(),
                        bigInteger("additionalExpenses").notNull(),
                        bigInteger("medianMaterialCost").notNull(),
                        bigInteger("minMaterialCost").notNull(),
                        integer("materialCostSampleSize").notNull(),
                        bigInteger("medianSalePrice").notNull(),
                        bigInteger("minSalePrice").notNull(),
                        integer("salePriceSampleSize").notNull())
                        .primaryKeys("id"));
    }

    @Override
    public SaleStrategy insert(final SaleStrategy saleStrategy) {
        final String INSERT_STATEMENT = "INSERT INTO saleStrategy "
                + "(productCount, product, marginTimestamp, additionalExpenses, medianMaterialCost, minMaterialCost, "
                + "materialCostSampleSize, medianSalePrice, minSalePrice, salePriceSampleSize) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_STATEMENT, new String[] { "id" });
                    ps.setInt(1, saleStrategy.getProductCount());
                    ps.setInt(2, saleStrategy.getProduct().getArmoryId().toInt());
                    ps.setLong(3, saleStrategy.getMarginTimestamp());
                    ps.setLong(4, saleStrategy.getAdditionalExpenses().toLong());
                    ps.setLong(5, saleStrategy.getMedianMaterialCost().toLong());
                    ps.setLong(6, saleStrategy.getMinMaterialCost().toLong());
                    ps.setInt(7, saleStrategy.getMaterialCostSampleSize());
                    ps.setLong(8, saleStrategy.getMedianSalePrice().toLong());
                    ps.setLong(9, saleStrategy.getMinSalePrice().toLong());
                    ps.setInt(10, saleStrategy.getSalePriceSampleSize());
                    return ps;
                }
            };

            getSimpleJdbcTemplate().getJdbcOperations().update(psc , keyHolder);

            int id = keyHolder.getKey().intValue();
            saleStrategy.setId(new SaleStrategyId(id));
            return saleStrategy;
        } catch (BadSqlGrammarException e) {
            getSimpleJdbcTemplate().update(INSERT_STATEMENT,
                    saleStrategy.getProductCount(),
                    saleStrategy.getProduct().getArmoryId().toInt(),
                    saleStrategy.getMarginTimestamp(),
                    saleStrategy.getAdditionalExpenses().toLong(),
                    saleStrategy.getMedianMaterialCost().toLong(),
                    saleStrategy.getMinMaterialCost().toLong(),
                    saleStrategy.getMaterialCostSampleSize(),
                    saleStrategy.getMedianSalePrice().toLong(),
                    saleStrategy.getMinSalePrice().toLong(),
                    saleStrategy.getSalePriceSampleSize()
                    );
            int id = getSimpleJdbcTemplate().queryForInt("CALL IDENTITY();");
            saleStrategy.setId(new SaleStrategyId(id));
            return saleStrategy;
        }
    }

    @Override
    public void delete(SaleStrategyId saleStrategyId) {
        getSimpleJdbcTemplate().update("DELETE FROM saleStrategy WHERE id=?",
                saleStrategyId.toInt());
    }

    @Override
    public List<SaleStrategy> getAll() {
        return getSimpleJdbcTemplate().query("SELECT id, productCount, product, marginTimestamp, additionalExpenses, "
                + "medianMaterialCost, minMaterialCost, materialCostSampleSize, medianSalePrice, minSalePrice, "
                + "salePriceSampleSize "
                + "FROM saleStrategy "
                + "ORDER BY (minSalePrice - medianMaterialCost - additionalExpenses) DESC;", saleStrategyMapper);
    }

    @Override
    public int getSize() {
        return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM saleStrategy");
    }

    @Override
    public void update(SaleStrategy saleStrategy) {
        getSimpleJdbcTemplate().update("UPDATE saleStrategy "
                + "SET productCount=?, product=?, marginTimestamp=?, additionalExpenses=?, medianMaterialCost=?, "
                + "minMaterialCost=?, materialCostSampleSize=?, medianSalePrice=?, minSalePrice=?, salePriceSampleSize=? "
                + "WHERE id=?",
                saleStrategy.getProductCount(),
                saleStrategy.getProduct().getArmoryId().toInt(),
                saleStrategy.getMarginTimestamp(),
                saleStrategy.getAdditionalExpenses().toLong(),
                saleStrategy.getMedianMaterialCost().toLong(),
                saleStrategy.getMinMaterialCost().toLong(),
                saleStrategy.getMaterialCostSampleSize(),
                saleStrategy.getMedianSalePrice().toLong(),
                saleStrategy.getMinSalePrice().toLong(),
                saleStrategy.getSalePriceSampleSize(),
                saleStrategy.getId().toInt());
    }

    @Override
    public SaleStrategy getBySaleStrategyId(SaleStrategyId saleStrategyId) {
        return getSimpleJdbcTemplate().queryForObject("SELECT id, productCount, product, marginTimestamp, additionalExpenses, "
                + "medianMaterialCost, minMaterialCost, materialCostSampleSize, medianSalePrice, minSalePrice, "
                + "salePriceSampleSize "
                + "FROM saleStrategy "
                + "WHERE id=?;", saleStrategyMapper, saleStrategyId.toInt());
    }

}
