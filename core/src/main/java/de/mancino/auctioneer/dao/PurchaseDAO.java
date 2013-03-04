package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.Purchase;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;

public interface PurchaseDAO extends Serializable {
    public Purchase insert(final Purchase purchase);

    public void deleteAllByPurchaseOrderId(final PurchaseOrderId purchaseOrderId);

    public List<Purchase> getAllByPurchaseOrderId(final PurchaseOrderId purchaseOrderId);

    public int getSize();
}
