package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class ASpellNumber extends Symbol {
    private long number;

    public ASpellNumber(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }


    private ASpellNumber(Symbol symbol, long number) throws ASpellParserException {
        super(symbol, false);
        this.number = number;
    }

    @Override
    protected void parse() throws ASpellParserException {
        number = Long.valueOf(expect(TokenType.NUMBER));
    }

    public long getLong() {
        return number;
    }

    public int getInt() {
        if(number > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int) number;
        }
        // No negative numbers yet!
    }

    public static ASpellNumber createConstant(Symbol symbol, long number) throws ASpellParserException {
        // TODO Auto-generated method stub
        return new ASpellNumber(symbol, number);
    }
}
