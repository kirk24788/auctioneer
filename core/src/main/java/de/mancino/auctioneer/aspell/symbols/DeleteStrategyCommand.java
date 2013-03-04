package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.dto.components.SaleStrategyId;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class DeleteStrategyCommand extends Symbol {
    private ASpellNumber saleId;

    public DeleteStrategyCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by CreateCommand!
        saleId = expect(ASpellNumber.class);
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        final StringBuffer sb = new StringBuffer();
        final SaleStrategyId id = new SaleStrategyId(saleId.getInt());
        sb.append("DELETE: ").append(getSaleStrategyBO().getById(id)).append("\n");
        getSaleStrategyBO().delete(id);
        return sb.toString();
    }
}
