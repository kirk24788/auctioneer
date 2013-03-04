package de.mancino.auctioneer.tasks;

import de.mancino.armory.Armory;
import de.mancino.armory.exceptions.RequestException;
import de.mancino.auctioneer.bo.PurchaseOrderBO;
import de.mancino.auctioneer.strategies.IBuyoutStrategy;

public class BuyoutPurchasingOrderTask extends AuctioneerTask {
    /**
     * Armory
     */
    private final Armory armory;

    /**
     * Price Watch BO
     */
    private final PurchaseOrderBO purchaseOrderBO;

    /**
     * Task for updating the price watches.
     * Used to add new price Watch Results to the database.
     *
     * @param priceWatchBO Price Watch BO
     * @param armory Armory
     */
    public BuyoutPurchasingOrderTask(final PurchaseOrderBO purchaseOrderBO, final Armory armory, final IBuyoutStrategy buyoutStrategy) {
        super(null);
        this.purchaseOrderBO = purchaseOrderBO;
        this.armory = armory;
        throw new RuntimeException("NOT IMPLEMENTED!");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "XXX";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runInternal() {

        try {
            System.err.println(armory.api.getAuctions().timestamp);
        } catch (RequestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        LOG.info("Updating PriceWatches...");
        for(PriceWatch priceWatch : priceWatchBO.listPriceWatches()) {
            final ArmoryId armoryId = priceWatch.getArmoryItem().getArmoryId();
            final ItemName itemName = priceWatch.getArmoryItem().getItemName();

            try {
                final long auctionTime = armory.api.getAuctions().timestamp;
                if(LOG.isDebugEnabled()) {
                    LOG.debug("Updating PriceWatch for: " + itemName);
                }
                final List<Auction> auctionItems = armory.api.getAuctions().alliance.auctions;
                final List<Price> prices = new LinkedList<Price>();
                for(Auction auction : auctionItems) {
                    if (auction.buyout > 0 && auction.item == armoryId.toInt()) {
                        prices.add(new Price(auction.buyout / auction.quantity));
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
            } catch (RequestException e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
        */
    }
}
