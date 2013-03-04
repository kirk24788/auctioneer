/*
 * UpdatePriceWatch.java 11.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

/**
 * Task for updating the sale strategies.
 *
 * @author mmancino
 */
public class RepairSaleStrategiesTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RepairSaleStrategiesTask.class);

    private static final long MAX_MATERIAL_COST_AGE_IN_MS = 7L * 24L * 60L * 60L * 1000L;


    /**
     * Price Watch BO
     */
    private final PriceWatchBO priceWatchBO;
    /**
     * Exception Log
     */
    private final SaleStrategyBO saleStrategyBO;


    /**
     * Task for updating the sale strategies.
     */
    public RepairSaleStrategiesTask(final PriceWatchBO priceWatchBO, final SaleStrategyBO saleStrategyBO, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.priceWatchBO = priceWatchBO;
        this.saleStrategyBO = saleStrategyBO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInternal() {
        try {
            LOG.info("Found {} sale strategies", saleStrategyBO.size());
            for(SaleStrategy saleStrategy : saleStrategyBO.getAllOrderedByProfit()) {
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
                    Currency medianMaterialCost = Currency.currency(medianMaterialCostTotal);
                    Currency minMaterialCost = Currency.currency(minMaterialCostTotal);
                    int materialCostSampleSize = materialCostSampleMinimum;

                    saleStrategy = saleStrategyBO.updateMargins(saleStrategy.getId(),
                            timestamp,
                            medianMaterialCost,
                            minMaterialCost,
                            materialCostSampleSize,
                            medianSalePrice,
                            minSalePrice,
                            salePriceSampleSize);
                    inserts++;
                }
                LOG.info("Inserted {} Samples: {}", inserts, saleStrategy);
            }
        } catch (PriceWatchDoesnNotExistException e) {
            LOG.error(e.getLocalizedMessage());
        }

    }

    private List<Long> getPriceWatchTimestamps(final ArmoryId armoryId) throws PriceWatchDoesnNotExistException {
        final Set<Long> timestamps = new HashSet<Long>();
        final PriceWatch priceWatch = priceWatchBO.findByArmoryId(armoryId);
        for(final PriceSample priceSample : priceWatch.getPriceSamples()) {
            if(priceSample.getTimeInMilliseconds()>=MAX_MATERIAL_COST_AGE_IN_MS) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "Repair SaleStrategies";
    }
}
