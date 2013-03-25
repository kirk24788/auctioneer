package de.mancino.auctioneer.tasks;

import de.mancino.auctioneer.bo.ArmoryCharacterBO;
import de.mancino.auctioneer.bo.ErrorLogBO;
import de.mancino.auctioneer.bo.PriceWatchBO;
import de.mancino.auctioneer.bo.RealmStatusBO;

public class RemoveOldDataTask extends AuctioneerTask {
    /**
     * Price Watch BO
     */
    private final PriceWatchBO priceWatchBO;

    private final ArmoryCharacterBO armoryCharacterBO;

    private final RealmStatusBO realmStatusBO;

    private final ErrorLogBO errorLogBO;

    public RemoveOldDataTask(final PriceWatchBO priceWatchBO, final ArmoryCharacterBO armoryCharacterBO,
            final RealmStatusBO realmStatusBO, final ErrorLogBO errorLogBO) {
        super(errorLogBO);
        this.priceWatchBO = priceWatchBO;
        this.armoryCharacterBO = armoryCharacterBO;
        this.realmStatusBO = realmStatusBO;
        this.errorLogBO = errorLogBO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskTitle() {
        return "Remove Old Data";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runInternal() {
        final long maxTimestamp = System.currentTimeMillis() - MILLIS_PER_MONTH;
        priceWatchBO.deleteOldPrices(maxTimestamp);
        armoryCharacterBO.deleteOldCashSamples(maxTimestamp);
        realmStatusBO.deleteOldStatus(System.currentTimeMillis() - MILLIS_PER_DAY); // Status gets updated too often !
        errorLogBO.remove(System.currentTimeMillis() - (2L*MILLIS_PER_DAY));
    }

}
