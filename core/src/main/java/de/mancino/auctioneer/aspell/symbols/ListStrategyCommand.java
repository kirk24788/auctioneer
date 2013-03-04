package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.dto.SaleStrategy;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class ListStrategyCommand extends Symbol {
    public ListStrategyCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by ListCommand!
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        StringBuffer sb = new StringBuffer();
        sb.append("STRATEGY LIST:\n");
        for(SaleStrategy saleStrategy : getSaleStrategyBO().getAllOrderedByProfit()) {
            sb.append(saleStrategy).append("\n");
        }
        return sb.toString();
    }
}
