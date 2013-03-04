package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class Category extends Symbol {
    private String category;


    public Category(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        category = expect(TokenType.CATEGORY);
    }

    /**
     * @return the categoryUpperCase
     */
    public String getCategoryUpperCase() {
        return category.toUpperCase();
    }
}
