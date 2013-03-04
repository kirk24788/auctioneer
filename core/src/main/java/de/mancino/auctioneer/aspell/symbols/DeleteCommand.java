package de.mancino.auctioneer.aspell.symbols;

import de.mancino.auctioneer.exceptions.ASpellParserException;
import de.mancino.auctioneer.exceptions.ASpellRuntimeError;

public class DeleteCommand extends Symbol {
    private Category category;
    private Symbol createCommand;

    DeleteCommand(Symbol parent) throws ASpellParserException {
        super(parent);
    }

    @Override
    protected void parse() throws ASpellParserException {
        category = new Category(this);
        switch(category.getCategoryUpperCase()) {
        case "STRATEGY":
            createCommand = expect(DeleteStrategyCommand.class);
            break;
        case "FARMING":
            createCommand = expect(DeleteFarmingCommand.class);
            break;
        case "SERVERSTATUS":
            throw new ASpellParserException("Can't DELETE SERVERSTATUS!");
        default:
            throw new IllegalStateException("Unsupported Category: " + category.getCategoryUpperCase());
        }
    }

    @Override
    public String execute() throws ASpellRuntimeError {
        return createCommand.execute();
    }
}
