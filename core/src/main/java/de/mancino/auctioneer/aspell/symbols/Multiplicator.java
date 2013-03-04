package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class Multiplicator extends Symbol {
    private ASpellNumber multiplicator;

    public Multiplicator(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        multiplicator = expect(ASpellNumber.class);
        expect(TokenType.MULTIPLICATOR);
    }

    public ASpellNumber getMultiplicator() {
        return multiplicator;
    }
}
