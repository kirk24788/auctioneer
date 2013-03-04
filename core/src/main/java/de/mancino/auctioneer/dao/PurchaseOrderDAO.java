package de.mancino.auctioneer.dao;

import java.io.Serializable;
import java.util.List;

import de.mancino.auctioneer.dto.PurchaseOrder;
import de.mancino.auctioneer.dto.components.PurchaseOrderId;

public interface PurchaseOrderDAO extends Serializable {
    public PurchaseOrder insert(final PurchaseOrder purchaseOrder);
    public void delete(final PurchaseOrder purchaseOrder);
    public void setActiveStatus(final PurchaseOrder purchaseOrder, final boolean isActive);
    public List<PurchaseOrder> getAll();
    public List<PurchaseOrder> getAllByActiveStatus(final boolean isActive);
    public PurchaseOrder getByPurchaseOrderId(final PurchaseOrderId purchaseOrderId);
    public int getSize();
}

