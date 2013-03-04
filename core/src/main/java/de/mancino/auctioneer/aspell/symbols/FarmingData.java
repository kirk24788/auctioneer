package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class FarmingData extends Symbol {
    private ASpellString name;
    private ItemIdentifier iconItem;
    private ItemList loot;
    private ASpellNumber additionalProfit;

    public FarmingData(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        name = expect(ASpellString.class);
        iconItem = null;
        if(optionalMatch(TokenType.KEYWORD, "ICON")) {
            iconItem = expect(ItemIdentifier.class);
        }
        expect(TokenType.KEYWORD, "YIELDS");
        loot = expect(ItemList.class);

        if(optionalMatch(TokenType.KEYWORD, "ADD")) {
            additionalProfit = expect(ASpellNumber.class);
        } else {
            additionalProfit = number(0);
        }
        if(iconItem==null) {
            iconItem = loot.getItemList().get(0).getItemIdentifier();
        }
    }

    public ItemIdentifier getIconItem() {
        return iconItem;
    }

    public ASpellString getName() {
        return name;
    }

    public ItemList getLoot() {
        return loot;
    }

    public ASpellNumber getAdditionalProfit() {
        return additionalProfit;
    }
}
