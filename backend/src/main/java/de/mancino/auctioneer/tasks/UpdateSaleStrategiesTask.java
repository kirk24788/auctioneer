/*
 * UpdatePriceWatch.java 11.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.bo.SaleStrategyBO;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

/**
 * Task for updating the sale strategies.
 *
 * @author mmancino
 */
public class UpdateSaleStrategiesTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpdateSaleStrategiesTask.class);

    private static final long MAX_MATERIAL_COST_AGE_IN_MS = 7L * 24L * 60L * 60L * 1000L;


    /**
     * Price Watch BO
     */
    private final PriceWatchBO priceWatchBO;
    /**
     * Exception Log
     */
    private final SaleStrategyBO saleStrategyBO;

    private long lastStrategyUpdate = 0L;

    /**
     * Task for updating the sale strategies.
     */
    public UpdateSaleStrategiesTask(final PriceWatchBO priceWatchBO, final SaleStrategyBO saleStrategyBO, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.priceWatchBO = priceWatchBO;
        this.saleStrategyBO = saleStrategyBO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInternal() {
        final long lastPriceWatchUpdate = getLastPriceWatchUpdate();
        if (lastStrategyUpdate >= lastPriceWatchUpdate) {
            LOG.info("No new Auction Data (age: {} min): ", getAgeInMinutes(lastPriceWatchUpdate));
        } else  {
            try {
                lastStrategyUpdate = lastPriceWatchUpdate;
                LOG.info("Found {} sale strategies", saleStrategyBO.size());
                for(SaleStrategy saleStrategy : saleStrategyBO.getAllOrderedByProfit()) {
                    final PriceWatch productPriceWatch = priceWatchBO.findByArmoryId(saleStrategy.getProduct().getArmoryId());
                    final PriceSample productPriceSample = productPriceWatch.getPriceSamples().get(productPriceWatch.getPriceSamples().size()-1);

                    Currency medianSalePrice = productPriceSample.getMedianPrice();
                    Currency minSalePrice = productPriceSample.getMinimumPrice();
                    int salePriceSampleSize = productPriceSample.getSampleSize();

                    long medianMaterialCostTotal = 0L;
                    long minMaterialCostTotal = 0L;
                    int materialCostSampleMinimum = Integer.MAX_VALUE;

                    for(final SaleStrategyMaterial ssm : saleStrategy.getMaterials()) {
                        final long itemCount = ssm.getItemCount();
                        final List<PriceSample> priceSamples = priceWatchBO.findByArmoryId(ssm.getItem().getArmoryId()).getPriceSamples();
                        final PriceSample priceSample = priceSamples.get(priceSamples.size()-1);

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
                            lastPriceWatchUpdate,
                            medianMaterialCost,
                            minMaterialCost,
                            materialCostSampleSize,
                            medianSalePrice,
                            minSalePrice,
                            salePriceSampleSize);
                    LOG.info("Updated: {}", saleStrategy);
                }
            } catch (PriceWatchDoesnNotExistException e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
    }

    private long getLastPriceWatchUpdate() {
        final PriceWatch priceWatch = priceWatchBO.listAllHighlighted().get(0);
        final List<PriceSample> priceSamples = priceWatch.getPriceSamples();
        return priceSamples.get(priceSamples.size()-1).getTimeInMilliseconds();
    }

    private long getAgeInMinutes(long auctionTime) {
        long ageInMs = System.currentTimeMillis()-auctionTime;
        return ageInMs / 60000L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "Update SaleStrategies";
    }
}
