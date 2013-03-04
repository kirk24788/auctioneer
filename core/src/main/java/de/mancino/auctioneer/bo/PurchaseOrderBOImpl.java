package de.mancino.auctioneer.bo;

import java.util.List;

import de.mancino.auctioneer.dao.PurchaseDAO;
import de.mancino.auctioneer.dao.PurchaseOrderDAO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.Purchase;
import de.mancino.auctioneer.dto.PurchaseOrder;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;
import de.mancino.auctioneer.strategies.BuyoutStrategy;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {
    private final PurchaseDAO purchaseDAO;
    private final PurchaseOrderDAO purchaseOrderDAO;

    public PurchaseOrderBOImpl(final PurchaseOrderDAO purchaseOrderDAO, final PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    @Override
    public PurchaseOrder createPurchaseOrder(ArmoryItem armoryItem, Currency maxPrice, int itemCount, BuyoutStrategy strategy) {
        final PurchaseOrder purchaseOrder = new PurchaseOrder(armoryItem, itemCount, maxPrice);
        purchaseOrder.setActive(true);
        purchaseOrder.setStrategy(strategy);
        purchaseOrder.setTimeInMilliseconds(System.currentTimeMillis());
        return purchaseOrderDAO.insert(purchaseOrder);
    }

    @Override
    public List<PurchaseOrder> listPurchaseOrders() {
        return purchaseOrderDAO.getAll();
    }

    @Override
    public List<PurchaseOrder> listActivePurchaseOrders() {
        return purchaseOrderDAO.getAllByActiveStatus(true);
    }

    @Override
    public List<PurchaseOrder> listInactivePurchaseOrders() {
        return purchaseOrderDAO.getAllByActiveStatus(false);
    }

    @Override
    public void addPurchase(PurchaseOrderId purchaseOrderId, Currency price, int itemCount, String buyerName) {
        final Purchase purchase = new Purchase();
        purchase.setBuyerName(buyerName);
        purchase.setItemCount(itemCount);
        purchase.setPrice(price);
        purchase.setPurchaseOrderId(purchaseOrderId);
        purchase.setTimeInMilliseconds(System.currentTimeMillis());
        purchaseDAO.insert(purchase);
    }

    @Override
    public void deactivatePurchaseOrder(PurchaseOrderId purchaseOrderId) {
        purchaseOrderDAO.setActiveStatus(purchaseOrderDAO.getByPurchaseOrderId(purchaseOrderId), false);
    }

    @Override
    public void activatePurchaseOrder(PurchaseOrderId purchaseOrderId) {
        purchaseOrderDAO.setActiveStatus(purchaseOrderDAO.getByPurchaseOrderId(purchaseOrderId), true);
    }

}
