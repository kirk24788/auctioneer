package de.mancino.auctioneer.bo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import de.mancino.auctioneer.dao.ArmoryItemDAO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryIcon;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.auctioneer.exceptions.ArmoryItemAlreadyExistingException;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;
import de.mancino.auctioneer.exceptions.WowHeadItemAlreadyExistingException;

public class ArmoryItemBOImpl implements ArmoryItemBO {
    private static final long serialVersionUID = 1L;

    /**
     * Logger instance of this class.
     */
    private static final Log LOG = LogFactory.getLog(ArmoryItemBOImpl.class);

    private final ArmoryItemDAO armoryItemDAO;

    protected ArmoryItemBOImpl() {
        this.armoryItemDAO = null;
    }

    public ArmoryItemBOImpl(final ArmoryItemDAO armoryItemDAO) {
        this.armoryItemDAO = armoryItemDAO;
    }


    @Override
    public ArmoryItem createArmoryItem(final de.mancino.armory.json.api.item.Item item) throws ArmoryItemAlreadyExistingException {
        try {
            LOG.info("Creating Armory Item for: " + item.name);
            final ArmoryItem armoryItem = new ArmoryItem(item);
            armoryItemDAO.insert(armoryItem);
            return armoryItem;
        } catch (final DataIntegrityViolationException e) {
            LOG.error("Error creating Armory Item", e);
            throw new ArmoryItemAlreadyExistingException(item);
        }
    }

    @Override
    public ArmoryItem createWowHeadItem(final de.mancino.armory.xml.wowhead.item.Item item) throws WowHeadItemAlreadyExistingException {
        try {
            LOG.info("Creating WoW-Head Item for: " + item.name);
            final ArmoryItem armoryItem = new ArmoryItem(new ArmoryId(item.id), new ItemName(item.name), new ArmoryIcon(item.icon));
            armoryItemDAO.insert(armoryItem);
            return armoryItem;
        } catch (final DataIntegrityViolationException e) {
            LOG.error("Error creating WoW-Head Item", e);
            throw new WowHeadItemAlreadyExistingException(item);
        }
    }


    @Override
    public ArmoryItem findByItemName(final ItemName itemName) throws ArmoryItemDoesNotExistException {
        try {
            if(LOG.isDebugEnabled()) {
                LOG.debug("Searching Armory Item by Name '" + itemName + "'");
            }
            return armoryItemDAO.getByItemName(itemName);
        } catch (final EmptyResultDataAccessException e) {
            LOG.warn("Armory Item for " + itemName + " not found!");
            throw new ArmoryItemDoesNotExistException(itemName);
        }
    }

    @Override
    public ArmoryItem findByArmoryId(final ArmoryId armoryId) throws ArmoryItemDoesNotExistException {
        try {
            if(LOG.isDebugEnabled()) {
                LOG.debug("Searching Armory Item by ID " + armoryId.toInt());
            }
            return armoryItemDAO.getByArmoryId(armoryId);
        } catch (final EmptyResultDataAccessException e) {
            LOG.warn("Armory Item with with ID " + armoryId.toInt() + " not found!");
            throw new ArmoryItemDoesNotExistException(armoryId);
        }
    }


    @Override
    public List<ArmoryItem> listArmoryItems() {
        return armoryItemDAO.getAll();
    }
}