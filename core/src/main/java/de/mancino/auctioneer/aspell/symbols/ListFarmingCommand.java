package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.dto.FarmStrategy;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class ListFarmingCommand extends Symbol {
    public ListFarmingCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by ListCommand!
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        StringBuffer sb = new StringBuffer();
        sb.append("FARMING LIST:\n");
        for(FarmStrategy farmStrategy : getFarmStrategyBO().getAllOrderedByProfit()) {
            sb.append(farmStrategy).append("\n");
        }
        return sb.toString();
    }
}