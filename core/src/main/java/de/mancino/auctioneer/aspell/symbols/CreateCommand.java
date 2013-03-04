package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class CreateCommand extends Symbol {
    private Category category;
    private Symbol createCommand;

    CreateCommand(Symbol parent) throws ASpellParserException {
        super(parent);
    }

    @Override
    protected void parse() throws ASpellParserException {
        category = new Category(this);
        switch(category.getCategoryUpperCase()) {
        case "STRATEGY":
            createCommand = expect(CreateStrategyCommand.class);
            break;
        case "FARMING":
            createCommand = expect(CreateFarmingCommand.class);
            break;
        case "SERVERSTATUS":
            throw new ASpellParserException("Can't CREATE SERVERSTATUS!");
        default:
            throw new IllegalStateException("Unsupported Category: " + category.getCategoryUpperCase());
        }
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        return createCommand.execute();
    }
}
