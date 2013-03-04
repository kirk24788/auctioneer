/*
 * UpdatePriceWatch.java 11.10.2010
 *
 * Copyright (c) 2010 1&1 Internet AG. All rights reserved.
 *
 * $Id$
 */
package de.mancino.auctioneer.tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.armory.Armory;
import de.mancino.armory.exceptions.RequestException;
import de.mancino.armory.json.api.auction.Auction;
import de.mancino.armory.json.api.auction.Auctions;
import de.mancino.auctioneer.bo.ArmoryItemBO;
import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;
import de.mancino.auctioneer.exceptions.PriceWatchAlreadyExistingException;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;
import de.mancino.auctioneer.exceptions.WowHeadItemAlreadyExistingException;

/**
 * Task for updating the price watches.
 * Used to add new rpice Watch Results to the database.
 *
 * @author mmancino
 */
public class UpdatePriceWatchTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePriceWatchTask.class);

    /**
     * Armory
     */
    private final Armory armory;

    /**
     * Price Watch BO
     */
    private final PriceWatchBO priceWatchBO;

    /**
     * Armory Item BO
     */
    private final ArmoryItemBO armoryItemBO;

    private long lastAuctionTime = 0L;

    /**
     * Task for updating the price watches.
     * Used to add new rpice Watch Results to the database.
     *
     * @param priceWatchBO Price Watch BO
     * @param armory Armory
     */
    public UpdatePriceWatchTask(final PriceWatchBO priceWatchBO, final ArmoryItemBO armoryItemBO, final Armory armory, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.priceWatchBO = priceWatchBO;
        this.armoryItemBO = armoryItemBO;
        this.armory = armory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runInternal() {
        try {
            final Auctions auctions = armory.api.getAuctions();
            final List<Auction> auctionItems = auctions.alliance.auctions;
            final long auctionTime = auctions.timestamp;
            int itemsCreated = 0;
            if(lastAuctionTime == auctionTime) {
                LOG.info("No new Auction Data (age: {} min): ", getAgeInMinutes(auctionTime));
            } else {
                LOG.info("Found {} auctions", auctionItems.size());
                final List<ArmoryId> uniqueIds = getUniqueArmoryIds(auctionItems);
                LOG.info("Found {} unique items", uniqueIds.size());
                for(ArmoryId armoryId : uniqueIds) {
                    // Get/Create ArmoryItem
                    ArmoryItem armoryItem;
                    try {
                        armoryItem = armoryItemBO.findByArmoryId(armoryId);
                    } catch (ArmoryItemDoesNotExistException e) {
                        itemsCreated++;
                        armoryItem = armoryItemBO.createWowHeadItem(armory.wowhead.getItem(armoryId.toInt()));
                    }
                    // Get/Create PriceWatch
                    PriceWatch priceWatch;
                    try {
                        priceWatch = priceWatchBO.findByArmoryId(armoryId);
                    } catch (PriceWatchDoesnNotExistException e) {
                        priceWatch = priceWatchBO.createPriceWatch(armoryItem);
                    }
                    // Calc prices
                    final List<Currency> prices = new LinkedList<Currency>();
                    for(Auction auction : auctionItems) {
                        if (auction.buyout > 0 && auction.item == armoryId.toInt()) {
                            prices.add(new Currency(auction.buyout / auction.quantity));
                        }
                    }
                    boolean alreadyInList = false;
                    for(final PriceSample sample : priceWatch.getPriceSamples()) {
                        if(sample.getTimeInMilliseconds() == auctionTime) {
                            alreadyInList=true;
                        }
                    }
                    if(!alreadyInList) {
                        priceWatchBO.addPriceSample(priceWatch.getId(), prices, auctionTime);
                    }
                }
                if(itemsCreated > 0) {
                    LOG.info("Found {} unknown items which were added to the database", itemsCreated);
                }
                lastAuctionTime = auctionTime;
            }
        } catch (RequestException e) {
            errorLogBO.addException(e);
            LOG.error(e.getLocalizedMessage());
        } catch (WowHeadItemAlreadyExistingException e) {
            // Will NEVER occur it this is the only priceWatchTask on the db!
            errorLogBO.addException(e);
            LOG.error(e.getLocalizedMessage());
        } catch (PriceWatchAlreadyExistingException e) {
            // Will NEVER occur it this is the only priceWatchTask on the db!
            errorLogBO.addException(e);
            LOG.error(e.getLocalizedMessage());
        }
    }

    private long getAgeInMinutes(long auctionTime) {
        long ageInMs = System.currentTimeMillis()-auctionTime;
        return ageInMs / 60000L;
    }

    private List<ArmoryId> getUniqueArmoryIds(final List<Auction> auctions) {
        final Set<Integer> itemIds = new HashSet<Integer>();

        for(final Auction auction : auctions) {
            itemIds.add(auction.item);
        }

        final List<ArmoryId> armoryIds = new ArrayList<ArmoryId>(itemIds.size());
        for(Integer itemId : itemIds) {
            armoryIds.add(new ArmoryId(itemId));
        }
        return armoryIds;
    }

    /**
     * {@inheritDoc}
     */
     @Override
     public String getTaskTitle() {
         return "Update PriceWatch";
     }
}
