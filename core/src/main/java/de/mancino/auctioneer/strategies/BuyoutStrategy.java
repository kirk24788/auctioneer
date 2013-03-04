package de.mancino.auctioneer.strategies;

public enum BuyoutStrategy {
    CHEAPEST_FIRST(new BuyoutCheapestFirst()),
    DOUBLE_THRESHOLD(new BuyoutWithDoubleThreshold());
    
    public final IBuyoutStrategy strategy;

    BuyoutStrategy(final IBuyoutStrategy strategy) {
        this.strategy = strategy;
    }
}
