package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.exceptions.ASpellParserException;

public class ASpellNumberFinal extends ASpellNumber {
    private long number;

    public ASpellNumberFinal(final Symbol symbol, final long number) throws ASpellParserException {
        super(symbol);
        this.number = number;
    }

    @Override
    protected void parse() throws ASpellParserException {
    }

    @Override
    public long getLong() {
        return number;
    }

    @Override
    public int getInt() {
        if(number > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int) number;
        }
        // No negative numbers yet!
    }
}
