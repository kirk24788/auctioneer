package de.mancino.auctioneer.bo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import de.mancino.auctioneer.dao.PriceSampleDAO;
import de.mancino.auctioneer.dao.PriceWatchDAO;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.PriceWatch;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.dto.components.PriceWatchId;
import de.mancino.auctioneer.exceptions.PriceWatchAlreadyExistingException;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

public class PriceWatchBOImpl implements PriceWatchBO {
    private static final long serialVersionUID = 1L;

    /**
     * Logger instance of this class.
     */
    private static final Log LOG = LogFactory.getLog(PriceWatchBOImpl.class);

    private final PriceWatchDAO priceWatchDAO;
    private final PriceSampleDAO priceSampleDAO;

    public PriceWatchBOImpl(final PriceWatchDAO priceWatchDao, final PriceSampleDAO priceSampleDAO) {
        this.priceWatchDAO = priceWatchDao;
        this.priceSampleDAO = priceSampleDAO;
    }


    protected PriceWatchBOImpl() {
        this.priceWatchDAO = null;
        this.priceSampleDAO = null;
    }

    @Override
    public PriceWatch createPriceWatch(final ArmoryItem armoryItem) throws PriceWatchAlreadyExistingException {
        try {
            LOG.info("Creating Price-Watch for: " + armoryItem.getItemName());
            return priceWatchDAO.insert(new PriceWatch(armoryItem));
        } catch (final DataIntegrityViolationException e) {
            LOG.error("Error creating Price-Watch", e);
            throw new PriceWatchAlreadyExistingException(armoryItem.getItemName());
        }
    }


    @Override
    public void addPriceSample(final PriceWatchId priceWatchId, final List<Currency> pricesList, final long timestamp) {
        priceSampleDAO.insert(new PriceSample(priceWatchId, pricesList, timestamp));
    }

    @Override
    public void deletePriceWatch(final PriceWatch priceWatch) {
        priceSampleDAO.deleteAllByPriceWatchId(priceWatch.getId());
        priceWatchDAO.delete(priceWatch);
    }

    
    @Override
    public PriceWatch findByArmoryId(final ArmoryId armoryId) throws PriceWatchDoesnNotExistException {
        try {
            if(LOG.isDebugEnabled()) {
                LOG.debug("Searching Price-Watch for Item with ID " + armoryId.toInt());
            }
            return priceWatchDAO.getByArmoryId(armoryId);
        } catch (final EmptyResultDataAccessException e) {
            LOG.warn("Price Wach for Item with ID " + armoryId.toInt() + " not found!");
            throw new PriceWatchDoesnNotExistException(armoryId);
        }
    }


    @Override
    public List<PriceWatch> listPriceWatches() {
        return priceWatchDAO.getAll();
    }


    @Override
    public void deleteOldPrices(final long maxTimestamp) {
        priceSampleDAO.deleteAllByMaxTimestamp(maxTimestamp);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setHighlighted(PriceWatch priceWatch, boolean highlighted) {
        priceWatch.setHighlighted(highlighted);
        priceWatchDAO.update(priceWatch);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PriceWatch> listAllHighlighted() {
        return priceWatchDAO.getAllByHighlighted(true);
    }
}