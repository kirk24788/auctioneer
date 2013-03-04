package de.mancino.auctioneer.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.dao.SaleStrategyDAO;
import de.mancino.auctioneer.dao.SaleStrategyMarginDAO;
import de.mancino.auctioneer.dao.SaleStrategyMaterialDAO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMargin;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.SaleStrategyId;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

public class SaleStrategyBOImpl implements SaleStrategyBO {
    private static final long serialVersionUID = 1L;

    private static final long MAX_MATERIAL_COST_AGE_IN_MS = 7L * 24L * 60L * 60L * 1000L;
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SaleStrategyBOImpl.class);

    private final SaleStrategyDAO saleStrategyDAO;
    private final SaleStrategyMaterialDAO saleStrategyMaterialDAO;
    private final SaleStrategyMarginDAO saleStrategyMarginDAO;
    private final PriceWatchBO priceWatchBO;

    public SaleStrategyBOImpl(final SaleStrategyDAO saleStrategyDAO, final SaleStrategyMaterialDAO saleStrategyMaterialDAO,
            final SaleStrategyMarginDAO saleStrategyMarginDAO, final PriceWatchBO priceWatchBO) {
        this.saleStrategyDAO = saleStrategyDAO;
        this.saleStrategyMaterialDAO = saleStrategyMaterialDAO;
        this.saleStrategyMarginDAO = saleStrategyMarginDAO;
        this.priceWatchBO = priceWatchBO;
    }

    protected SaleStrategyBOImpl() {
        this(null, null, null, null);
    }

    @Override
    public SaleStrategy createSaleStrategy(final ArmoryItem product, final int productCount, final Currency additionalExpenses,
            final SaleStrategyMaterial... saleStrategyMaterials) {
        return addSaleStrategyMaterials(createSaleStrategy(product, productCount, additionalExpenses), saleStrategyMaterials);
    }

    @Override
    public SaleStrategy createSaleStrategy(final ArmoryItem product, final int productCount, final Currency additionalExpenses) {
        final SaleStrategy saleStrategy = new SaleStrategy(productCount, product);
        saleStrategy.setAdditionalExpenses(additionalExpenses);
        return saleStrategyDAO.insert(saleStrategy);
    }

    @Override
    public void initMargins(SaleStrategyId saleStrategyId) {
        SaleStrategy saleStrategy = getById(saleStrategyId);
        try {
            final PriceWatch productPriceWatch = priceWatchBO.findByArmoryId(saleStrategy.getProduct().getArmoryId());

            final List<Long> productTimestamps = getPriceWatchTimestamps(saleStrategy.getProduct().getArmoryId());
            final List<List<Long>> materialTimestamps = new ArrayList<List<Long>>();
            for(SaleStrategyMaterial ssm : saleStrategy.getMaterials()) {
                materialTimestamps.add(getPriceWatchTimestamps(ssm.getItem().getArmoryId()));
            }
            final List<Long> validTimestamps = unionTimestamps(productTimestamps, materialTimestamps);
            Collections.sort(validTimestamps);
            int inserts = 0;
            for(final Long timestamp : validTimestamps) {
                final PriceSample productPriceSample = getPriceSample(productPriceWatch.getPriceSamples(), timestamp);
                Currency medianSalePrice = productPriceSample.getMedianPrice();
                Currency minSalePrice = productPriceSample.getMinimumPrice();
                int salePriceSampleSize = productPriceSample.getSampleSize();

                long medianMaterialCostTotal = 0L;
                long minMaterialCostTotal = 0L;
                int materialCostSampleMinimum = Integer.MAX_VALUE;
                for(final SaleStrategyMaterial ssm : saleStrategy.getMaterials()) {
                    final long itemCount = ssm.getItemCount();
                    final List<PriceSample> priceSamples = priceWatchBO.findByArmoryId(ssm.getItem().getArmoryId()).getPriceSamples();
                    final PriceSample priceSample = getPriceSample(priceSamples, timestamp);

                    medianMaterialCostTotal += (itemCount * priceSample.getMedianPrice().toLong());
                    minMaterialCostTotal  += (itemCount * priceSample.getMinimumPrice().toLong());
                    if(materialCostSampleMinimum > priceSample.getSampleSize()) {
                        materialCostSampleMinimum = priceSample.getSampleSize();
                    }
                }
                if(materialCostSampleMinimum != Integer.MAX_VALUE) {
                    Currency medianMaterialCost = Currency.currency(medianMaterialCostTotal);
                    Currency minMaterialCost = Currency.currency(minMaterialCostTotal);
                    int materialCostSampleSize = materialCostSampleMinimum;

                    saleStrategy = updateMargins(saleStrategy.getId(),
                            timestamp,
                            medianMaterialCost,
                            minMaterialCost,
                            materialCostSampleSize,
                            medianSalePrice,
                            minSalePrice,
                            salePriceSampleSize);
                    inserts++;
                }
            }
            LOG.info("Inserted {} Samples: {}", inserts, saleStrategy);
        } catch (PriceWatchDoesnNotExistException e) {
            LOG.warn("At least one Pricewatch didn't exist: %s", e.getMessage());
        }
    }

    @Override
    public SaleStrategy addSaleStrategyMaterials(final SaleStrategy saleStrategy, final SaleStrategyMaterial... saleStrategyMaterials) {
        for(SaleStrategyMaterial ssm : saleStrategyMaterials) {
            ssm.setSaleStrategyId(saleStrategy.getId());
            saleStrategyMaterialDAO.insert(ssm);
        }
        return saleStrategyDAO.getBySaleStrategyId(saleStrategy.getId());
    }

    @Override
    public List<SaleStrategy> getAllOrderedByProfit() {
        final List<SaleStrategy> sortedList = new ArrayList<SaleStrategy>(saleStrategyDAO.getAll());
        Collections.sort(sortedList, new Comparator<SaleStrategy>() {
            @Override
            public int compare(SaleStrategy o1, SaleStrategy o2) {
                long v1 = o1.getProfit().toLong();
                long v2 = o2.getProfit().toLong();
                if(v1 > v2) {
                    return -1;
                } else if(v1 < v2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return Collections.unmodifiableList(sortedList);
    }

    @Override
    public List<SaleStrategy> getAllOrderedBySafeProfit() {
        final List<SaleStrategy> sortedList = new ArrayList<SaleStrategy>(saleStrategyDAO.getAll());
        Collections.sort(sortedList, new Comparator<SaleStrategy>() {
            @Override
            public int compare(SaleStrategy o1, SaleStrategy o2) {
                long v1 = o1.getSafeProfit().toLong();
                long v2 = o2.getSafeProfit().toLong();
                if(v1 > v2) {
                    return -1;
                } else if(v1 < v2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return Collections.unmodifiableList(sortedList);
    }

    @Override
    public SaleStrategy updateMargins(SaleStrategyId saleStrategyId, long marginTimestamp,
            Currency medianMaterialCost, Currency minMaterialCost, int materialCostSampleSize,
            Currency medianSalePrice, Currency minSalePrice, int salePriceSampleSize) {
        final SaleStrategyMargin saleStrategyMargin = new SaleStrategyMargin(saleStrategyId, marginTimestamp,
                medianMaterialCost, minMaterialCost, materialCostSampleSize, medianSalePrice, minSalePrice, salePriceSampleSize);
        return updateMargins(saleStrategyId, saleStrategyMargin);
    }

    @Override
    public SaleStrategy updateMargins(SaleStrategyId saleStrategyId, SaleStrategyMargin saleStrategyMargin) {
        final SaleStrategy saleStrategy = saleStrategyDAO.getBySaleStrategyId(saleStrategyId);
        final long lastTimestamp = saleStrategy.getMarginTimestamp();
        if(lastTimestamp<saleStrategyMargin.getMarginTimestamp()) {
            saleStrategyMarginDAO.insert(saleStrategyMargin);
        }

        saleStrategy.setMarginTimestamp(saleStrategyMargin.getMarginTimestamp());
        saleStrategy.setMedianMaterialCost(saleStrategyMargin.getMedianMaterialCost());
        saleStrategy.setMinMaterialCost(saleStrategyMargin.getMinMaterialCost());
        saleStrategy.setMaterialCostSampleSize(saleStrategyMargin.getMaterialCostSampleSize());
        saleStrategy.setMedianSalePrice(saleStrategyMargin.getMedianSalePrice());
        saleStrategy.setMinSalePrice(saleStrategyMargin.getMinSalePrice());
        saleStrategy.setSalePriceSampleSize(saleStrategyMargin.getSalePriceSampleSize());

        saleStrategyDAO.update(saleStrategy);

        return saleStrategy;
    }

    @Override
    public int size() {
        return saleStrategyDAO.getSize();
    }

    @Override
    public SaleStrategy getById(SaleStrategyId saleStrategyId) {
        return saleStrategyDAO.getBySaleStrategyId(saleStrategyId);
    }

    private List<Long> getPriceWatchTimestamps(final ArmoryId armoryId) throws PriceWatchDoesnNotExistException {
        final Set<Long> timestamps = new HashSet<Long>();
        final PriceWatch priceWatch = priceWatchBO.findByArmoryId(armoryId);
        for(final PriceSample priceSample : priceWatch.getPriceSamples()) {
            final long age = System.currentTimeMillis() - priceSample.getTimeInMilliseconds();
            if(age>=MAX_MATERIAL_COST_AGE_IN_MS) {
                timestamps.add(priceSample.getTimeInMilliseconds());
            }
        }
        final List<Long> sortedTimestamps = new ArrayList<Long>(timestamps);
        Collections.sort(sortedTimestamps);
        return sortedTimestamps;
    }

    private PriceSample getPriceSample(final List<PriceSample> priceSamples, final long timestamp) {
        for(PriceSample sample : priceSamples) {
            if(sample.getTimeInMilliseconds() == timestamp) {
                return sample;
            }
        }
        throw new RuntimeException("This shouldn't happen!");
    }

    private List<Long> unionTimestamps(final List<Long> product, List<List<Long>> materials) {
        final List<Long> union = new ArrayList<Long>(product);
        for(List<Long> material : materials) {
            union.retainAll(material);
        }
        return union;
    }

    @Override
    public void delete(SaleStrategyId saleStrategyId) {
        saleStrategyDAO.delete(saleStrategyId);
        saleStrategyMaterialDAO.deleteAllBySaleStrategyId(saleStrategyId);
        saleStrategyMarginDAO.deleteAllBySaleStrategyId(saleStrategyId);
    }
}
