package de.mancino.auctioneer.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.dao.FarmStrategyDAO;
import de.mancino.auctioneer.dao.FarmStrategyLootDAO;
import de.mancino.auctioneer.dao.FarmStrategyProfitDAO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.FarmStrategyProfit;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.FarmStrategyId;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

public class FarmStrategyBOImpl implements FarmStrategyBO {
    private static final long serialVersionUID = 1L;

    private static final long MAX_LOOT_PROFIT_AGE_IN_MS = 7L * 24L * 60L * 60L * 1000L;
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FarmStrategyBOImpl.class);

    private final FarmStrategyDAO farmStrategyDAO;
    private final FarmStrategyLootDAO farmStrategyLootDAO;
    private final FarmStrategyProfitDAO farmStrategyProfitDAO;
    private final PriceWatchBO priceWatchBO;

    public FarmStrategyBOImpl(final FarmStrategyDAO farmStrategyDAO, final FarmStrategyLootDAO farmStrategyLootDAO,
            final FarmStrategyProfitDAO farmStrategyProfitDAO, final PriceWatchBO priceWatchBO) {
        this.farmStrategyDAO = farmStrategyDAO;
        this.farmStrategyLootDAO = farmStrategyLootDAO;
        this.farmStrategyProfitDAO = farmStrategyProfitDAO;
        this.priceWatchBO = priceWatchBO;
    }

    protected FarmStrategyBOImpl() {
        this(null, null, null, null);
    }


    @Override
    public FarmStrategy createFarmStrategy(final ArmoryItem iconItem, final String name, final Currency additionalProfits,
            List<FarmStrategyLoot> loot) {
        FarmStrategy fs = new FarmStrategy(name, iconItem, additionalProfits);
        LOG.info("createFarmStrategy: {}", fs);
        fs = farmStrategyDAO.insert(fs);
        for(FarmStrategyLoot l : loot) {
            addLoot(fs.getId(), l);
        }
        return getById(fs.getId());
    }

    @Override
    public void addLoot(final FarmStrategyId farmStrategyId, final FarmStrategyLoot loot) {
        farmStrategyLootDAO.insert(new FarmStrategyLoot(farmStrategyId, loot.getItemCount(), loot.getItem()));
    }

    @Override
    public FarmStrategy getById(FarmStrategyId farmStrategyId) {
        return farmStrategyDAO.getById(farmStrategyId);
    }

    @Override
    public List<FarmStrategy> getAllOrderedByProfit() {
        final List<FarmStrategy> sortedList = new ArrayList<FarmStrategy>(farmStrategyDAO.getAll());
        Collections.sort(sortedList, new Comparator<FarmStrategy>() {
            @Override
            public int compare(FarmStrategy o1, FarmStrategy o2) {
                long v1 = o1.getTotalProfit().toLong();
                long v2 = o2.getTotalProfit().toLong();
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
    public List<FarmStrategy> getAllOrderedBySafeProfit() {
        final List<FarmStrategy> sortedList = new ArrayList<FarmStrategy>(farmStrategyDAO.getAll());
        Collections.sort(sortedList, new Comparator<FarmStrategy>() {
            @Override
            public int compare(FarmStrategy o1, FarmStrategy o2) {
                long v1 = o1.getTotalSafeProfit().toLong();
                long v2 = o2.getTotalSafeProfit().toLong();
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
    public FarmStrategy updateProfits(FarmStrategyId farmStrategyId, long timestamp,
            Currency medianSalePrice, Currency minSalePrice, int salePriceSampleSize) {
        final FarmStrategy farmStrategy = farmStrategyDAO.getById(farmStrategyId);
        if(timestamp>farmStrategy.getLatestProfit().getProfitTimestamp()) {
            final FarmStrategyProfit profit = new FarmStrategyProfit(farmStrategy.getId(),
                    timestamp, medianSalePrice, minSalePrice, salePriceSampleSize);
            farmStrategyProfitDAO.insert(profit);
        }

        return farmStrategyDAO.getById(farmStrategyId);
    }

    @Override
    public int size() {
        return farmStrategyDAO.getSize();
    }

    @Override
    public FarmStrategy initProfits(FarmStrategyId farmStrategyId) {
        LOG.info("initProfits: FarmStrategy ID:{}", farmStrategyId.toInt());
        FarmStrategy farmStrategy = getById(farmStrategyId);
        try {

            final List<List<Long>> materialTimestamps = new ArrayList<List<Long>>();
            for(FarmStrategyLoot fsl : farmStrategy.getLoot()) {
                materialTimestamps.add(getPriceWatchTimestamps(fsl.getItem().getArmoryId()));
            }
            final List<Long> validTimestamps = unionTimestamps(materialTimestamps);
            Collections.sort(validTimestamps);
            int inserts = 0;
            for(final Long timestamp : validTimestamps) {
                long medianProfitTotal = 0L;
                long minProfitTotal = 0L;
                int profitSampleMinimum = Integer.MAX_VALUE;
                for(final FarmStrategyLoot fsl : farmStrategy.getLoot()) {
                    final long itemCount = fsl.getItemCount();
                    final List<PriceSample> priceSamples = priceWatchBO.findByArmoryId(fsl.getItem().getArmoryId()).getPriceSamples();
                    final PriceSample priceSample = getPriceSample(priceSamples, timestamp);

                    medianProfitTotal += (itemCount * priceSample.getMedianPrice().toLong());
                    minProfitTotal  += (itemCount * priceSample.getMinimumPrice().toLong());
                    if(profitSampleMinimum > priceSample.getSampleSize()) {
                        profitSampleMinimum = priceSample.getSampleSize();
                    }
                }
                if(profitSampleMinimum != Integer.MAX_VALUE) {
                    Currency medianMaterialCost = Currency.currency(medianProfitTotal);
                    Currency minMaterialCost = Currency.currency(minProfitTotal);
                    int materialCostSampleSize = profitSampleMinimum;

                    final FarmStrategyProfit profit = new FarmStrategyProfit(farmStrategy.getId(),
                            timestamp, medianMaterialCost, minMaterialCost, materialCostSampleSize);
                    farmStrategyProfitDAO.insert(profit);
                    inserts++;
                }
            }
            LOG.info("Inserted {} Samples: {}", inserts, farmStrategy);
        } catch (PriceWatchDoesnNotExistException e) {
            LOG.warn("At least one Pricewatch didn't exist: %s", e.getMessage());
        }
        return farmStrategy;
    }



    private List<Long> getPriceWatchTimestamps(final ArmoryId armoryId) throws PriceWatchDoesnNotExistException {
        final Set<Long> timestamps = new HashSet<Long>();
        final PriceWatch priceWatch = priceWatchBO.findByArmoryId(armoryId);
        for(final PriceSample priceSample : priceWatch.getPriceSamples()) {
            final long age = System.currentTimeMillis() - priceSample.getTimeInMilliseconds();
            if(age>=MAX_LOOT_PROFIT_AGE_IN_MS) {
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

    private List<Long> unionTimestamps(List<List<Long>> materials) {
        final List<Long> union = new ArrayList<Long>(materials.get(0));
        for(int i=1 ; i < materials.size() ; i++) {
            List<Long> material = materials.get(i);
            union.retainAll(material);
        }
        return union;
    }

    @Override
    public void delete(FarmStrategyId farmStrategyId) {
        farmStrategyDAO.delete(farmStrategyId);
        farmStrategyLootDAO.deleteAllByFarmStrategyId(farmStrategyId);
        farmStrategyProfitDAO.deleteAllByFarmStrategyId(farmStrategyId);
    }

}
