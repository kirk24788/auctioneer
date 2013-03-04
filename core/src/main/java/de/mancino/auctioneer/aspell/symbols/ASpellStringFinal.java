package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.exceptions.ASpellParserException;

public class ASpellStringFinal extends ASpellString {
    private String string;
    public ASpellStringFinal(final Symbol symbol, final String string) throws ASpellParserException {
        super(symbol);
        this.string = string;
    }

    @Override
    protected void parse() throws ASpellParserException {
    }

    @Override
    public String getString() {
        return string;
    }
}
