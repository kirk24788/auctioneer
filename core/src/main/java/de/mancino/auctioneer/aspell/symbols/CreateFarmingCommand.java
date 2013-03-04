package de.mancino.auctioneer.aspell.symbols;

import java.util.ArrayList;
import java.util.List;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.dto.FarmStrategyLoot;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class CreateFarmingCommand extends Symbol {
    private FarmingData farmingData;

    public CreateFarmingCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by CreateCommand!
        farmingData = expect(FarmingData.class);
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        final StringBuffer sb = new StringBuffer();
        final ArmoryItem iconItem = farmingData.getIconItem().getArmoryItem();
        final String name = farmingData.getName().getString();
        final Currency additionalProfits = Currency.currency(farmingData.getAdditionalProfit().getLong());
        FarmStrategy farmingStrategy = getFarmStrategyBO().createFarmStrategy(iconItem, name, additionalProfits, lootList());
        farmingStrategy = getFarmStrategyBO().initProfits(farmingStrategy.getId());
        sb.append("CREATED: ").append(farmingStrategy).append("\n");
        return sb.toString();
    }

    private List<FarmStrategyLoot> lootList() {
        List<FarmStrategyLoot> loot = new ArrayList<FarmStrategyLoot>();
        for(Item l : farmingData.getLoot().getItemList()) {
            loot.add(new FarmStrategyLoot(null, l.getMultiplicator().getInt(), l.getItemIdentifier().getArmoryItem()));
        }
        return loot;
    }
}
