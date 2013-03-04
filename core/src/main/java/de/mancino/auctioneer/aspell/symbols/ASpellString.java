package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class ASpellString extends Symbol {
    private String string;

    public ASpellString(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        final String quotedString = expect(TokenType.STRING);
        string = quotedString.substring(1, quotedString.length()-1);
    }

    public String getString() {
        return string;
    }
}
