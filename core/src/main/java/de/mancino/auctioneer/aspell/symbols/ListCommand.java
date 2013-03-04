package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class ListCommand extends Symbol {
    private Category category;
    private Symbol listCommand;

    ListCommand(Symbol parent) throws ASpellParserException {
        super(parent);
    }

    @Override
    protected void parse() throws ASpellParserException {
        category = new Category(this);
        switch(category.getCategoryUpperCase()) {
        case "STRATEGY":
            listCommand = expect(ListStrategyCommand.class);
            break;
        case "FARMING":
            listCommand = expect(ListFarmingCommand.class);
            break;
        case "SERVERSTATUS":
            listCommand = expect(ListServerStatusCommand.class);
            break;
        case "CHARACTERS":
            listCommand = expect(ListCharactersCommand.class);
            break;
        default:
            throw new IllegalStateException("Unsupported Category: " + category.getCategoryUpperCase());
        }
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        return listCommand.execute();
    }
}
