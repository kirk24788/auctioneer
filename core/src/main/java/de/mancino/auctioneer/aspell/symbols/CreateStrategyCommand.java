package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.dto.SaleStrategyMaterial;
import de.mancino.auctioneer.dto.components.Currency;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class CreateStrategyCommand extends Symbol {
    private StrategyData strategyData;

    public CreateStrategyCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by CreateCommand!
        strategyData = expect(StrategyData.class);
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        final StringBuffer sb = new StringBuffer();
        final ArmoryItem product = strategyData.getProduct().getItemIdentifier().getArmoryItem();
        final int productCount = strategyData.getProduct().getMultiplicator().getInt();
        final Currency additionalExpenses = new Currency(strategyData.getAdditionalExpenses().getLong());
        final SaleStrategy saleStrategy = getSaleStrategyBO().createSaleStrategy(product, productCount, additionalExpenses);

        for(final Item item : strategyData.getMaterials().getItemList()) {
            final int itemCount = item.getMultiplicator().getInt();
            final ArmoryItem armoryItem = item.getItemIdentifier().getArmoryItem();
            final SaleStrategyMaterial ssm = new SaleStrategyMaterial(saleStrategy.getId(), itemCount, armoryItem);
            getSaleStrategyBO().addSaleStrategyMaterials(saleStrategy, ssm);
        }
        getSaleStrategyBO().initMargins(saleStrategy.getId());
        sb.append("CREATED: ").append(getSaleStrategyBO().getById(saleStrategy.getId())).append("\n");
        return sb.toString();
    }
}
