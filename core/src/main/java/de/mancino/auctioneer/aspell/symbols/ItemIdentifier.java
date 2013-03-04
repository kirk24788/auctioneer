package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.dto.ArmoryItem;
import de.mancino.auctioneer.dto.components.ArmoryId;
import de.mancino.auctioneer.dto.components.ItemName;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ArmoryItemDoesNotExistException;

public class ItemIdentifier extends Symbol {
    private ArmoryItem armoryItem;

    public ItemIdentifier(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        if(lookAheadType(TokenType.NUMBER)) {
            armoryItem = findById(expect(ASpellNumber.class));
        } else {
            armoryItem = findByName(expect(ASpellString.class));
        }
    }


    private ArmoryItem findByName(ASpellString aspellString) throws ArmoryItemDoesNotExistException {
        final ItemName itemName = new ItemName(aspellString.getString());
        return getArmoryItemBO().findByItemName(itemName);
    }

    private ArmoryItem findById(final ASpellNumber aspellNumber) throws ArmoryItemDoesNotExistException {
        final ArmoryId armoryId = new ArmoryId((int)aspellNumber.getLong());
        return getArmoryItemBO().findByArmoryId(armoryId);
    }

    public ArmoryItem getArmoryItem() {
        return armoryItem;
    }
}
