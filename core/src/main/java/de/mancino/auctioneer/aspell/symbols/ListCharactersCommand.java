package de.mancino.auctioneer.aspell.symbols;

import java.util.List;

import de.mancino.auctioneer.aspell.token.TokenType;
import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;
import de.mancino.auctioneer.exceptions.InvalidTokenException;
import de.mancino.auctioneer.exceptions.UnexpectedEndException;

public class ListCharactersCommand extends Symbol {
    private enum Order {NAME, ILVL, LEVEL}
    private Order order;

    public ListCharactersCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        try {
            if(lookAheadData("BY")) {
                expect(TokenType.KEYWORD, "BY");
                final String orderBy = expect(TokenType.KEYWORD, "LEVEL", "NAME", "ILVL");
                switch(orderBy.toUpperCase()) {
                case "NAME":
                    order = Order.NAME;
                    break;
                case "LEVEL":
                    order = Order.LEVEL;
                    break;
                case "ILVL":
                    order = Order.ILVL;
                    break;
                default:
                    throw new InvalidTokenException(null, "LEVEL", "NAME", "ILVL");
                }
            }
        } catch (UnexpectedEndException e) {
            // NO ORDER BY - use default
            order = Order.NAME;
        }
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        StringBuffer sb = new StringBuffer();
        sb.append("CHARACTER LIST:\n");
        for(ArmoryCharacter armoryCharacter : getCharacterList()) {
            sb.append(armoryCharacter).append("\n");
        }
        return sb.toString();
    }

    private List<ArmoryCharacter> getCharacterList() {
        if(order==null) {
            return getArmoryCharacterBO().listArmoryCharactersByName();
        }
        switch (order) {
        case NAME:
            return getArmoryCharacterBO().listArmoryCharactersByName();
        case LEVEL:
            return getArmoryCharacterBO().listArmoryCharactersByLevel();

        case ILVL:
            return getArmoryCharacterBO().listArmoryCharactersByItemLevel();
        default:
            throw new IllegalStateException();
        }
    }
}