package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.dto.ArmoryCharacter;
import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class ListCharactersCommand extends Symbol {
    public ListCharactersCommand(Symbol symbol) throws ASpellParserException {
        super(symbol);
    }

    @Override
    protected void parse() throws ASpellParserException {
        // Category already chomped by ListCommand!
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        StringBuffer sb = new StringBuffer();
        sb.append("FARMING LIST:\n");
        for(ArmoryCharacter armoryCharacter : getArmoryCharacterBO().listArmoryCharacters()) {
            sb.append(armoryCharacter).append("\n");
        }
        return sb.toString();
    }
}