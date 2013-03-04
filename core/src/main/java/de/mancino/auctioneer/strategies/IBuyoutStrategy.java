package de.mancino.auctioneer.strategies;

import java.util.List;

import de.mancino.armory.json.api.auction.Auction;
import de.mancino.auctioneer.dto.PurchaseOrder;

public interface IBuyoutStrategy {
    public List<Auction> filterCandidates(final List<Auction> allAuctions, final PurchaseOrder purchasingOrder);
}
