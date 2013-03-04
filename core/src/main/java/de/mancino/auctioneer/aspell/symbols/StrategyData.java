package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class StrategyData extends Symbol {
    private Item product;
    private ItemList materials;
    private ASpellNumber additionalExpenses;

    public StrategyData(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        product = expect(Item.class);
        expect(TokenType.KEYWORD, "FROM");
        materials = expect(ItemList.class);
        if(optionalMatch(TokenType.KEYWORD, "ADD")) {
            additionalExpenses = expect(ASpellNumber.class);
        } else {
            additionalExpenses = number(0);
        }
    }
    public Item getProduct() {
        return product;
    }
    public ItemList getMaterials() {
        return materials;
    }
    public ASpellNumber getAdditionalExpenses() {
        return additionalExpenses;
    }
}
