package de.mancino.auctioneer.bo;

import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PurchaseOrder;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.auctioneer.strategies.BuyoutStrategy;

public interface PurchaseOrderBO {
    public PurchaseOrder createPurchaseOrder(final ArmoryItem armoryItem, final Currency maxPrice, final int itemCount, final BuyoutStrategy strategy);
    public List<PurchaseOrder> listPurchaseOrders();
    public List<PurchaseOrder> listActivePurchaseOrders();
    public List<PurchaseOrder> listInactivePurchaseOrders();
    public void addPurchase(final PurchaseOrderId purchaseOrderId, final Currency price, final int itemCount, final String buyerName);
    public void deactivatePurchaseOrder(final PurchaseOrderId purchaseOrderId);
    public void activatePurchaseOrder(final PurchaseOrderId purchaseOrderId);
}
