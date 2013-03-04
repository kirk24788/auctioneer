package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class Item extends Symbol {
    private ASpellNumber multiplicator;
    private ItemIdentifier itemIdentifier;

    public Item(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        if(lookAheadType(TokenType.MULTIPLICATOR, 1)) {
            multiplicator = expect(ASpellNumber.class);
            expect(TokenType.MULTIPLICATOR);
        }
        itemIdentifier = expect(ItemIdentifier.class);
    }

    public ItemIdentifier getItemIdentifier() {
        return itemIdentifier;
    }

    public ASpellNumber getMultiplicator() {
        return multiplicator;
    }
}
