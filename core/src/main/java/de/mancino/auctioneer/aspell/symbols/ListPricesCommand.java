package de.mancino.auctioneer.aspell.symbols;

import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.PriceSample;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;
import de.mancino.auctioneer.exceptions.PriceWatchDoesnNotExistException;

public class ListPricesCommand extends Symbol {
    private ItemList items;

    public ListPricesCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        items = expect(ItemList.class);

    }

    @Override
    public String execute() throws ASpellRuntimeError {
        StringBuffer sb = new StringBuffer();
        sb.append("PRICE(S):\n");
        long minTotal = 0L;
        long medianTotal = 0L;
        for(final Item item : items.getItemList()) {
            long count = item.getMultiplicator() != null ? item.getMultiplicator().getInt() : 1L;
            ArmoryItem armoryItem = item.getItemIdentifier().getArmoryItem();
            sb.append(count).append(" x ").append(armoryItem.toString()).append(": ");
            try {
                List<PriceSample> samples = getPriceWatchBO().findByArmoryId(armoryItem.getArmoryId()).getPriceSamples();
                PriceSample lastSample = samples.get(samples.size()-1);
                Currency min = new Currency(lastSample.getMinimumPrice().toLong() * count);
                minTotal += lastSample.getMinimumPrice().toLong() * count;
                Currency median = new Currency(lastSample.getMedianPrice().toLong() * count);
                medianTotal += lastSample.getMedianPrice().toLong() * count;
                sb.append(min).append(" (Minimum); ").append(median).append(" (Median)");
            } catch (PriceWatchDoesnNotExistException e) {
                sb.append("NO PRICE INFO!");
            }
            sb.append("\n");
        }
        if (items.getItemList().size()>1) {
            sb.append("TOTAL: ").append(new Currency(minTotal)).append(" (Minimum); ")
            .append(new Currency(medianTotal)).append(" (Median)\n");

        }
        return sb.toString();
    }
}