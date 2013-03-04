package de.mancino.auctioneer.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mancino.armory.Armory;
import de.mancino.armory.json.api.auction.Auction;
import de.mancino.armory.json.api.auction.Auctions;
import de.mancino.auctioneer.bo.BargainBO;
import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.dto.Bargain;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;

public class UpdateBargainsTask extends AuctioneerTask {
    /**
     * Logger instance of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UpdateBargainsTask.class);

    private final BargainBO dealBO;
    private final PriceWatchBO priceWatchBO;
    private final Armory armory;

    public UpdateBargainsTask(final BargainBO dealBO, final PriceWatchBO priceWatchBO, final Armory armory, ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.dealBO = dealBO;
        this.armory = armory;
        this.priceWatchBO = priceWatchBO;
    }

    @Override
    public String getTaskTitle() {
        return "Update Bargains";
    }

    @Override
    protected void runInternal() {
        try {
            final Auctions auctions = armory.api.getAuctions();
            final List<Auction> auctionItems = auctions.alliance.auctions;
            final long timestamp = auctions.timestamp;

            LOG.info("Found {} auctions", auctionItems.size());
            final List<ArmoryId> uniqueIds = getUniqueArmoryIds(auctionItems);
            LOG.info("Found {} unique items", uniqueIds.size());

            List<ArmoryId> highlighted = new ArrayList<>();
            for(final PriceWatch pw : priceWatchBO.listAllHighlighted()) {
                highlighted.add(pw.getArmoryItem().getArmoryId());
            }

            Map<ArmoryId,Bargain> dealMap = new HashMap<ArmoryId, Bargain>();
            for(ArmoryId armoryId : uniqueIds) {
                if(highlighted.contains(armoryId)) {
                    final PriceWatch priceWatch = priceWatchBO.findByArmoryId(armoryId);
                    final Bargain deal = new Bargain(priceWatch.getArmoryItem());
                    dealMap.put(armoryId, deal);
                    deal.setEqualizedMedian(new Currency(getEqualizedMedian(priceWatch)));
                    deal.setEqualizedMinimum(new Currency(getEqualizedMinimum(priceWatch)));
                    deal.setThresholdPrice(new Currency((long) (deal.getEqualizedMinimum().toLong() * 0.80)));
                    deal.setTimestamp(timestamp);
                }
            }
            for(final Auction auction : auctionItems) {
                ArmoryId aid = new ArmoryId(auction.item);
                if(auction.buyout>0 && dealMap.containsKey(aid)) {
                    final Bargain deal = dealMap.get(aid);
                    long price = auction.buyout / auction.quantity;
                    if (price <= deal.getThresholdPrice().toLong()) {
                        deal.addAuction(auction.quantity, auction.buyout);
                    }
                }
            }


            dealBO.removeAll();
            int dealCount = 0;
            for(final Bargain deal : dealMap.values()) {
                if(deal.getItemCount() > 0) {
                    dealBO.add(deal);
                    dealCount++;
                }
            }
            LOG.info("Found {} bargains", dealCount);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
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


    private static Long getEqualizedMinimum(final PriceWatch priceWatch) {
        List<Long> minimumValues = new ArrayList<Long>();

        for(final PriceSample ps : priceWatch.getPriceSamples()) {
            if(ps.getTimeInMilliseconds() >= getMaxTimestamp()) {
                minimumValues.add(ps.getMinimumPrice().toLong());
            }
        }
        return median(minimumValues);
    }

    private static Long getEqualizedMedian(final PriceWatch priceWatch) {
        List<Long> medianValues = new ArrayList<Long>();

        for(final PriceSample ps : priceWatch.getPriceSamples()) {
            if(ps.getTimeInMilliseconds() >= getMaxTimestamp()) {
                medianValues.add(ps.getMedianPrice().toLong());
            }
        }
        return median(medianValues);
    }

    private static Long median(final List<Long> values) {
        Collections.sort(values);
        if(values.size() == 0) {
            throw new IllegalStateException("No minimum prices found!");
        }
        int pos = values.size()/2;
        if(values.size()%2==0) {
            return (values.get(pos-1) + values.get(pos)) / 2L;
        } else {
            return values.get(pos);
        }
    }

    private static long getMaxTimestamp() {
        return System.currentTimeMillis() - (2L * 24L * 60L * 60L * 1000L);
    }
}
