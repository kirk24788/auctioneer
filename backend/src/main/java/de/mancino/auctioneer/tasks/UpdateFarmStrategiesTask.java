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
import de.mancino.auctioneer.bo.FarmStrategyBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

/**
 * Task for updating the sale strategies.
 *
 * @author mmancino
 */
public class UpdateFarmStrategiesTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpdateFarmStrategiesTask.class);


    /**
     * Price Watch BO
     */
    private final PriceWatchBO priceWatchBO;
    /**
     * Exception Log
     */
    private final FarmStrategyBO farmStrategyBO;

    private long lastStrategyUpdate = 0L;

    /**
     * Task for updating the sale strategies.
     */
    public UpdateFarmStrategiesTask(final PriceWatchBO priceWatchBO, final FarmStrategyBO farmStrategyBO, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.priceWatchBO = priceWatchBO;
        this.farmStrategyBO = farmStrategyBO;
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
                LOG.info("Found {} farming strategies", farmStrategyBO.size());
                for(FarmStrategy farmStrategy : farmStrategyBO.getAllOrderedByProfit()) {
                    long medianProfitTotal = 0L;
                    long minProfitTotal = 0L;
                    int profitSampleMinimum = Integer.MAX_VALUE;
                    for(final FarmStrategyLoot fsl : farmStrategy.getLoot()) {
                        final long itemCount = fsl.getItemCount();
                        final List<PriceSample> priceSamples = priceWatchBO.findByArmoryId(fsl.getItem().getArmoryId()).getPriceSamples();
                        final PriceSample priceSample = priceSamples.get(priceSamples.size()-1);

                        medianProfitTotal += (itemCount * priceSample.getMedianPrice().toLong());
                        minProfitTotal  += (itemCount * priceSample.getMinimumPrice().toLong());
                        if(profitSampleMinimum > priceSample.getSampleSize()) {
                            profitSampleMinimum = priceSample.getSampleSize();
                        }

                    }
                    farmStrategy = farmStrategyBO.updateProfits(farmStrategy.getId(), lastStrategyUpdate,
                            new Currency(medianProfitTotal), new Currency(minProfitTotal), profitSampleMinimum);

                    LOG.info("Updated: {}", farmStrategy);
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
        return "Update FarmStrategies";
    }
}
