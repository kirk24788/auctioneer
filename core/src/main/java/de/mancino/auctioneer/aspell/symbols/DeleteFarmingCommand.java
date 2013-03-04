package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.dto.components.FarmStrategyId;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class DeleteFarmingCommand extends Symbol {
    private ASpellNumber farmingId;

    public DeleteFarmingCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by CreateCommand!
        farmingId = expect(ASpellNumber.class);
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        final StringBuffer sb = new StringBuffer();
        final FarmStrategyId id = new FarmStrategyId(farmingId.getInt());
        sb.append("DELETE: ").append(getFarmStrategyBO().getById(id)).append("\n");
        getFarmStrategyBO().delete(id);
        return sb.toString();
    }
}
