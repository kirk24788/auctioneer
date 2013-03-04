package de.mancino.auctioneer.aspell.symbols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.exceptions.ASpellParserException;

public class ItemList extends Symbol {
    private List<Item> itemList;

    public ItemList(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        itemList = new ArrayList<>();
        itemList.add(expect(Item.class));
        if(optionalMatch(TokenType.SEPERATOR,",")) {
            itemList.addAll(expect(ItemList.class).itemList);
        }
    }

    public List<Item> getItemList() {
        return Collections.unmodifiableList(itemList);
    }
}
